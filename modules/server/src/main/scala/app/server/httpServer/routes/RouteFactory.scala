package app.server.httpServer.routes

import akka.actor.ActorSystem
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

  val crudRouteFactory: CRUDRouteFactory =
    CRUDRouteFactory(persistenceModule, executionContext)

  private def allRoutes: Route =
    crudRouteFactory.route[User] ~
      getPostRoute[SumIntRoute]().route ~
      getStaticRoute(rootPageHtml)

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML

  // todo-next
  //    2) write JSDOM + Scala.js + Node.js based integration test

}
//  private def simplePostRouteHelloWorld: Route = {
//    post {
//      path( "hello_world" ) {
//        complete( "Hello world !" )
//      }
//    }
//  }
//
//  private def ping_pong: Route = {
//    post {
//      path( "ping_pong" ) { ctx =>
//        {
//          ctx.complete( {
//            val r1: RequestEntity = ctx.request.entity
//            val ctype: ContentType =
//              ctx.request.entity.httpEntity.contentType
//            val res =
//              s"""
//                   |
//                   | Request Entity is :
//                   | $r1
//                   |
//                   | Content type is:
//                   | $ctype
//                   |
//                   |
//                 """.stripMargin
//
//            println(
//              "ping pong route was called, we respond" +
//                "with the following string:\n+res"
//            )
//            res
//
//          } )
//        }
//      }
//    }
//  }
