package app.server.httpServer.routes.crud

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import app.server.httpServer.routes.crud.routes.{
  PersCommandRouteFactory,
  PersCommandRouteWithResponseWrapperFactory
}
import app.server.httpServer.routes.fileUploading.UploadFileRoute
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._
import io.circe.generic.auto._
import shared.crudRESTCallCommands.persActorCommands.crudCMDs.{
  GetAllEntityiesForUserPersActCmd,
  InsertEntityPersActCmd,
  UpdateEntitiesPersActorCmd,
  UpdateEntityPersActCmd
}
import shared.crudRESTCallCommands.persActorCommands.generalCmd.GeneralPersActorCmd

import scala.concurrent.ExecutionContextExecutor
import scala.language.postfixOps

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

  lazy val uploadFileRoute = UploadFileRouteImpl()

  private def allRoutes: Route =
    getStaticRoute(rootPageHtml) ~
      PersCommandRouteFactory[UpdateEntitiesPersActorCmd](actor).getRoute ~
      PersCommandRouteFactory[GetAllEntityiesForUserPersActCmd](actor).getRoute ~
      PersCommandRouteFactory[InsertEntityPersActCmd](
        actor
      ).getRoute ~
      PersCommandRouteFactory[UpdateEntityPersActCmd](
        actor
      ).getRoute ~
      uploadFileRoute.route ~
      PersCommandRouteWithResponseWrapperFactory[GeneralPersActorCmd](
        actor
      ).getRoute

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML
}
