package app.server.httpServer.routes.crud

import akka.actor.{ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.persistence.{PersistentActor, RecoveryCompleted}
import akka.util.Timeout
import app.server.httpServer.routes.crud.routes.PersCommandRouteFactory
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._
import shared.crudRESTCallCommands._
import shared.crudRESTCallCommands.persActorCommands.{
  GetAllEntityiesForUserPersActCmd,
  InsertEntityPersActCmd
}
import shared.testingData.TestDataStore

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps
import io.circe.generic.auto._
import shared.dataStorage.stateHolder.UserMap

case class RouteAssembler(
  implicit
  actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor) {

  lazy val actor: ActorRef = actorSystem.actorOf(
    Props(
      new PersistentActorImpl(
        "the_one_and_only_parsistent_actor"
      )
    )
  )

  val route: Route = allRoutes

  private def allRoutes: Route =
    getStaticRoute(rootPageHtml) ~
      PersCommandRouteFactory[GetAllEntityiesForUserPersActCmd](actor).getRoute ~
      PersCommandRouteFactory[InsertEntityPersActCmd](
        actor
      ).getRoute

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML
}
