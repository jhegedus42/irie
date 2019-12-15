package app.server.httpServer.routes.crud

import akka.actor.{
  ActorLogging,
  ActorRef,
  ActorSystem,
  Props
}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.persistence.{PersistentActor, RecoveryCompleted}
import akka.util.Timeout
import app.server.httpServer.routes.crud.routes.PersCommandRouteFactory
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._
import comm.crudRequests._
import comm.crudRequests.persActorCommands.{
  GetAllEntityiesForUser,
  InsertEntityIntoDataStore
}
import dataStorage.RefToEntityOwningUser
import dataStorage.stateHolder.UserMap
import testingData.TestDataStore

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps
import io.circe.generic.auto._

case class RouteAssembler(
  implicit
  actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor) {

  private def getActor(
    id: String,
    as: ActorSystem
  ) = as.actorOf(props(id))

  private def props(id: String): Props =
    Props(new PersistentActorImpl(id))

  val actor: ActorRef = getActor(
    "the_one_and_only_parsistent_actor",
    actorSystem
  )

  val route: Route = allRoutes

  private def allRoutes: Route =
    getStaticRoute(rootPageHtml) ~
      PersCommandRouteFactory[GetAllEntityiesForUser](actor).getRoute
  PersCommandRouteFactory[InsertEntityIntoDataStore](actor).getRoute

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML
}
