package app.server.httpServer.routes

import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.PostRouteFactory.getPostRoute
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.PersistenceService
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.{GetEntityLogic, InsertEntityHandler, UpdateEntityHandler}
import app.shared.comm.postRequests.{GetEntityReq, InsertNewEntityReq, UpdateEntityReq}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContextExecutor
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
