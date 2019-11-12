package app.server.httpServer.routes
import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import akka.actor.{ActorLogging, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._

import scala.language.postfixOps
import scala.reflect.ClassTag
import scala.concurrent.ExecutionContextExecutor
import akka.persistence.{PersistentActor, RecoveryCompleted}
import comm.crudRequests.Commands

class PersistentActorImpl(id: String)
    extends PersistentActor
    with ActorLogging {

  override def receiveCommand: Receive = {
    case Commands.ShutDown =>
      println("shutting down persistent actor")
      context.stop(self)

    case Commands.GetState =>
      println("do nothing")
  }

  override def persistenceId: String = id

  override def receiveRecover: Receive = {

    case RecoveryCompleted => {
      log.info(
        "Recovery completed \n\nState is:\n"
      )
    }

  }

}

import akka.actor.{ActorRef, ActorSystem, Props}

case class PersistentActorFactory(
  actorSystemForPersistentActor: ActorSystem) {

  private def getActor(
    id: String,
    as: ActorSystem
  ) = as.actorOf(props(id))

  private def props(id: String): Props =
    Props(new PersistentActorImpl(id))

  val actor: ActorRef = getActor(
    "the_one_and_only_parsistent_actor",
    actorSystemForPersistentActor
  )

}

case class RouteFactory(
  implicit actorSystem:     ActorSystem,
  executionContextExecutor: ExecutionContextExecutor) {

  val route: Route = allRoutes

  import io.circe.generic.auto._

  private def allRoutes: Route =
    getStaticRoute(rootPageHtml) //~
//      GetAllUsersSodiumRoute(paw).getRoute

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML

}
