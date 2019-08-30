package app.server.httpServer.routes

import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.PostRouteFactory.getPostRoute
import app.server.httpServer.routes.post.routeLogicImpl.{GetRouteLogic, InsertRouteLogic, UpdateRouteLogic}
import app.shared.comm.postRequests.{GetEntityReq, InsertNewEntityReq, UpdateEntityReq}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

case class CRUDRouteFactory(
                             persistenceModule: PersistentServiceProvider,
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

    implicit val insertRouteLogic = InsertRouteLogic(
      persistenceModule,
      decoder,
      encoder,
      implicitly[ClassTag[V]],
      executionContext
    )

    implicit val updateRouteLogic = UpdateRouteLogic(
      persistenceModule,
      decoder,
      encoder,
      implicitly[ClassTag[V]],
      executionContext
    )

    implicit val getRouteLogic =
      GetRouteLogic[V](persistenceModule, decoder, executionContext)

    // todo-next :
    //    1) write a simple CURL test in a .sh script <<<<====
    //       for the update entity route

    getPostRoute[UpdateEntityReq[V]].route ~
      getPostRoute[InsertNewEntityReq[V]].route ~
      getPostRoute[GetEntityReq[V]].route
  }

}
