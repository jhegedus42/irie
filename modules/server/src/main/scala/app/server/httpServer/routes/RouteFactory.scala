package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentType, RequestEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.PostRouteForAkkaHttpFactory._
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._
import app.shared.comm.postRequests.SumIntRoute
import app.shared.entity.entityValue.values.User

import scala.concurrent.ExecutionContextExecutor

private[httpServer] case class RouteFactory(actorSystem: ActorSystem) {

  private implicit lazy val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher

  val persistenceModule = PersistentServiceProvider(executionContext)

  val route: Route = allRoutes

  import io.circe.generic.auto._

  lazy val crudRouteFactory: CRUDRouteFactory =
    CRUDRouteFactory(persistenceModule, executionContext)

  private def allRoutes: Route =
    crudRouteFactory.route[User] ~
      getPostRoute[SumIntRoute]().route ~
      getStaticRoute(rootPageHtml) ~
      simplePostRouteHelloWorld ~
      ping_pong

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML

  // todo-now-2 :
  //  write akka-http-based, server side only tests


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
