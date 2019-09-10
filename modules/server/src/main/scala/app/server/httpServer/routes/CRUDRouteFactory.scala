package app.server.httpServer.routes

import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.PostRouteFactory.getPostRoute
import app.server.httpServer.routes.post.routeLogicImpl.{
  GetRL,
  InsertRL,
  UpdateRL
}
import app.shared.comm.postRequests.{
  GetEntityReq,
  InsertReq,
  UpdateReq
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

case class CRUDRouteFactory()(
//    implicit persistenceModule: PersistentServiceProvider, // todo-now - put something here
                           implicit
    executionContext:  ExecutionContextExecutor
) {
//  implicit val ec = executionContext: ExecutionContextExecutor
  def route[
      V <: EntityValue[V]: ClassTag
  ](
      implicit
      unTypedRefDecoder: Decoder[RefToEntityWithVersion[V]],
      encoder:           Encoder[Entity[V]],
      valueEncoder:      Encoder[V],
      decoder:           Decoder[Entity[V]],
      dpl:               Decoder[V]
  ): Route = {

    import io.circe.generic.auto._

    implicit val insertRouteLogic = InsertRL()

    implicit val updateRouteLogic = UpdateRL()

    implicit val getRouteLogic =
      GetRL[V]( dpl, executionContext)

    getPostRoute[UpdateReq[V]].route ~
      getPostRoute[InsertReq[V]].route ~
      getPostRoute[GetEntityReq[V]].route
  }

}
