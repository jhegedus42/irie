package app.server.httpServer.routes
import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import akka.actor.{ActorLogging, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._

import scala.language.postfixOps
import scala.reflect.ClassTag
import scala.concurrent.{ExecutionContextExecutor, Future}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import akka.util.Timeout
import comm.crudRequests.{Command, GetAllEntityiesForUser, RouteName, ShutDown}
import dataStorage.UserRef
import dataStorage.stateHolder.EntityStorage
import testingData.{TestDataStore, TestEntitiesForUsers}

class PersistentActorImpl(id: String)
    extends PersistentActor
    with ActorLogging {

  var state = TestDataStore.testData

  override def receiveCommand: Receive = {
    case ShutDown =>
      println("shutting down persistent actor")
      context.stop(self)

    case GetAllEntityiesForUser(userRef: UserRef, resp) => {
      println("user uuid is : $uuid")
      sender ! state.getUserMap(userRef)
    }
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

case class RouteFactory(
  implicit actorSystem:     ActorSystem,
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

  import io.circe.generic.auto._

  private def allRoutes: Route =
    getStaticRoute(rootPageHtml)

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML

  object GetEntitiesForUser {
    val rnProvider = implicitly[RouteName[GetAllEntityiesForUser]]
    val rn=rnProvider.getRouteName
    def getEntitiesRoute: Route = {
          post {
            path(rn) {
              entity(as[String]) { ??? // s: String =>.
              }
            }
          }
      //todo-now CONTINUE HERE
    }

    def getEntitiesFuture(msg:Command): Future[GetAllEntityiesForUser] =  {
      import scala.concurrent.Future
      import akka.pattern.ask
      import scala.concurrent.duration._
      implicit val timeout = Timeout(5 seconds)

      ask(actor, msg).mapTo[GetAllEntityiesForUser]

    }
  }
}
