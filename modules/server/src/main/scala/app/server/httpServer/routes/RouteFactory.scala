package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.PostRouteFactory._
import app.server.httpServer.routes.post.routeLogicImpl.logic.write.ResetServerStateLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._
import app.shared.comm.{ReadRequest, WriteRequest}
import app.shared.comm.postRequests.{GetAllUsersReq, ResetRequest, SumIntRoute}
import app.shared.entity.entityValue.values.User

import scala.concurrent.ExecutionContextExecutor

private[httpServer] case class RouteFactory(
  actorSystem: ActorSystem) {

  private implicit lazy val executionContext
    : ExecutionContextExecutor =
    actorSystem.dispatcher

  implicit val paw = PersistentActorWhisperer(actorSystem)

  val route: Route = allRoutes

  import io.circe.generic.auto._

  lazy val crudRouteFactory: CRUDRouteFactory = CRUDRouteFactory()

  private def allRoutes: Route =
    crudRouteFactory.route[User] ~
      getPostRoute[SumIntRoute]().route ~
      getStaticRoute(rootPageHtml) ~
      simplePostRouteHelloWorld ~
      ping_pong ~
      getPostRoute[ResetRequest]().route ~
      getPostRoute[GetAllUsersReq]().route

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML

  private def simplePostRouteHelloWorld: Route = {
    import akka.http.scaladsl.server.Directives._
    post {
      path("hello_world") {
        complete("Hello world !")
      }
    }
  }

  private def ping_pong: Route = {
    import akka.http.scaladsl.server.Directives._
    post {
      path("ping_pong") {
        entity(as[String]) { s =>
          complete(s)
        }
      }
    }
  }

}
