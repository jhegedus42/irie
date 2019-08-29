package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentType, RequestEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.PostRouteFactory._
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.PersistenceService
import app.server.httpServer.routes.post.{PostRoute, PostRouteFactory}
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.{
  GetEntityLogic,
  InsertEntityHandler,
  UpdateEntityHandler
}
import app.server.httpServer.routes.static.StaticRoutes._
import app.server.httpServer.routes.static.{IndexDotHtml, StaticRoutes}
import app.shared.comm.postRequests.{
  GetEntityReq,
  InsertNewEntityReq,
  SumIntPostRequest,
  UpdateEntityReq
}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.Entity
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

case class CRUDRouteFactory(
    persistenceModule: PersistenceService,
    executionContext:  ExecutionContextExecutor
) {
  def route[
      V <: EntityValue[V]: ClassTag
  ](
      implicit
      unTypedRefDecoder: Decoder[RefToEntityWithVersion[V]],
      encoder:           Encoder[Entity[V]],
      decoder:           Decoder[Entity[V]],
      dpl:               Decoder[V]
  ): Route = {

    //  todo-next-1 update entity route

    import io.circe.generic.auto._

    implicit val insertRouteLogic = InsertEntityHandler(
      persistenceModule,
      decoder,
      encoder,
      implicitly[ClassTag[V]],
      executionContext
    )

    implicit val updateRouteLogic = UpdateEntityHandler(
      persistenceModule,
      decoder,
      encoder,
      implicitly[ClassTag[V]],
      executionContext
    )

    implicit val getRouteLogic =
      GetEntityLogic[V](persistenceModule, decoder, executionContext)

    // todo-next :
    //    1) write a simple CURL test in a .sh script <<<<====
    //       for the update entity route

    getPostRoute[UpdateEntityReq[V]].route ~
      getPostRoute[InsertNewEntityReq[V]].route ~
      getPostRoute[GetEntityReq[V]].route
  }

}

private[httpServer] case class RouteFactory(actorSystem: ActorSystem) {

  private implicit lazy val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher

  val persistenceModule = PersistenceService(executionContext)

  val route: Route = allRoutes

  import io.circe.generic.auto._

  val crudRouteFactory: CRUDRouteFactory =
    CRUDRouteFactory(persistenceModule, executionContext)

  private def allRoutes: Route =
    crudRouteFactory.route[User] ~
      getPostRoute[SumIntPostRequest]().route ~
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
