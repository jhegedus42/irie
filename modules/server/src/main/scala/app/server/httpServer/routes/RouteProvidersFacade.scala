package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.persistenceProvider.PersistenceModule
import app.server.httpServer.routes.routeProviders.dynamicRouteProviders.{
  PostRoute,
  PostRouteFactory
}
import app.server.httpServer.routes.routeProviders.dynamicRouteProviders.serverLogicAsTypeClasses.instanceFactories.{
  GetEntityLogic,
  InsertEntityLogic,
  UpdateEntityLogic
}
import app.server.httpServer.routes.routeProviders.staticRouteProviders.{
  IndexDotHtml,
  StaticRoutes
}
import app.shared.comm.postRequests.{
  GetEntityReq,
  InsertNewEntityReq,
  SumIntPostRequest,
  UpdateEntityReq
}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.{Entity, RefToEntity}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

private[httpServer] case class RouteProvidersFacade(
    actorSystem: ActorSystem
) {

  private implicit lazy val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher

  val persistenceModule = PersistenceModule( executionContext )

  val route: Route = allRoutes

  import io.circe._
  import io.circe.generic.auto._

  private def allRoutes: Route = {

    val result: Route =
      crudEntityRoute[User] ~
        PostRouteFactory.createPostRoute[SumIntPostRequest]().route ~
        StaticRoutes.staticRootFactory( rootPageHtml )

    result
  }

  private def rootPageHtml: String = IndexDotHtml.getIndexDotHTML

  private def crudEntityRoute[V <: EntityValue[V]: ClassTag](
      implicit unTypedRefDecoder: Decoder[RefToEntity[V]],
      encoder:                    Encoder[Entity[V]],
      decoder:                    Decoder[Entity[V]],
      dpl:                        Decoder[V]
  ): Route = {

    //  todo-next-1 update entity route

    import io.circe.generic.auto._

    implicit val insertRouteLogic = InsertEntityLogic(
      persistenceModule,
      decoder,
      encoder,
      implicitly[ClassTag[V]],
      executionContext
    )

    implicit val updateRouteLogic = UpdateEntityLogic(
      persistenceModule,
      decoder,
      encoder,
      implicitly[ClassTag[V]],
      executionContext
    )

    implicit val getRouteLogic =
      GetEntityLogic[V]( persistenceModule, decoder, executionContext )

    // todo-now :
    //    1) write a simple CURL test in a .sh script <<<<====
    //       for the update entity route



      PostRouteFactory.createPostRoute[UpdateEntityReq[V]].route ~
      PostRouteFactory.createPostRoute[InsertNewEntityReq[V]].route ~
      PostRouteFactory.createPostRoute[GetEntityReq[V]].route
  }

  // todo-next
  //    2) write JSDOM + Scala.js + Node.js based integration test

}
