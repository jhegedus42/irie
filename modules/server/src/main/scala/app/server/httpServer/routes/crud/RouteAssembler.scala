package app.server.httpServer.routes.crud

import akka.actor.{ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.persistence.{PersistentActor, RecoveryCompleted}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import app.server.httpServer.routes.crud.routes.PersCommandRouteFactory
import app.server.httpServer.routes.fileUploading.UploadingRoute
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._
import shared.crudRESTCallCommands._
import shared.crudRESTCallCommands.persActorCommands.{
  GetAllEntityiesForUserPersActCmd,
  InsertEntityPersActCmd,
  UpdateEntityPersActCmd
}
import shared.testingData.TestDataStore

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps
import io.circe.generic.auto._
import shared.dataStorage.stateHolder.UserMap

case class RouteAssembler(
  implicit
  actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor,
  actorMaterializer:        ActorMaterializer) {

  lazy val actor: ActorRef = actorSystem.actorOf(
    Props(
      new PersistentActorImpl(
        "the_one_and_only_parsistent_actor"
      )
    )
  )

  val route: Route = allRoutes

  object ULR extends UploadingRoute {
    override implicit val system: ActorSystem = actorSystem

    override implicit def executor: ExecutionContextExecutor =
      executionContextExecutor

    override implicit val materializer: Materializer =
      actorMaterializer
  }

  private def allRoutes: Route =
    getStaticRoute(rootPageHtml) ~
      PersCommandRouteFactory[GetAllEntityiesForUserPersActCmd](actor).getRoute ~
      PersCommandRouteFactory[InsertEntityPersActCmd](
        actor
      ).getRoute ~
      PersCommandRouteFactory[UpdateEntityPersActCmd](
        actor
      ).getRoute ~ ULR.uploadFile

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML
}
