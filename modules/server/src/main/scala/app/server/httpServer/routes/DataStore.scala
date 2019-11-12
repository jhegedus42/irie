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
import comm.crudRequests.{Command, GetAllEntityiesForUser, JSONConvertable, RouteName, ShutDown}
import dataStorage.UserRef
import dataStorage.stateHolder.{EntityStorage, UserMap}
import testingData.{TestDataStore, TestEntitiesForUsers}

class PersistentActorImpl(id: String)
//    extends PersistentActor
extends PersistentActor
with ActorLogging {

  var state = TestDataStore.testData

  override def receiveCommand: Receive = {
    case ShutDown =>
      println("shutting down persistent actor")
      context.stop(self)

    case GetAllEntityiesForUser(userRef: UserRef, resp) => {
      println(s"user uuid is : ${userRef.uuid}")
      val umap: UserMap = state.getUserMap(userRef)
      sender ! GetAllEntityiesForUser(userRef,Some(umap))
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
    getStaticRoute(rootPageHtml)~
    GetEntitiesForUser.getEntitiesRoute

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML

  object GetEntitiesForUser {
    val rnProvider = implicitly[RouteName[GetAllEntityiesForUser]]
    val rn         = rnProvider.getRouteName

    def getEntitiesRoute: Route = {
      post {
        path(rn) {
          entity(as[String]) {
            s => {
              val i: JSONConvertable[GetAllEntityiesForUser] =
                implicitly[JSONConvertable[GetAllEntityiesForUser]]
              val getAllEntityiesForUser: GetAllEntityiesForUser = i.getObject(s)
              val f=getEntitiesFuture(getAllEntityiesForUser)
              val fs=f.map(x=>i.getJSON(x))
              complete(fs)
            }
          }
        }
      }
    }

    def getEntitiesFuture(
      msg: Command
    ): Future[GetAllEntityiesForUser] = {
      import scala.concurrent.Future
      import akka.pattern.ask
      import scala.concurrent.duration._
      implicit val timeout = Timeout(5 seconds)

      ask(actor, msg).mapTo[GetAllEntityiesForUser]

    }
  }
}
