package app.server.httpServer.routes

import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.PostRouteFactory.getPostRoute
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
import app.server.httpServer.routes.post.routeLogicImpl.crudLogic.{
  GetEntityLogic,
  InsertEntityLogic,
  UpdateEntityLogic
}
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

case class CRUDRouteFactory(
)(
  implicit
  paw:              PersistentActorWhisperer,
  executionContext: ExecutionContextExecutor) {

//  implicit val ec = executionContext: ExecutionContextExecutor
  def route[V <: EntityValue[V]: ClassTag](
    implicit
    unTypedRefDecoder: Decoder[RefToEntityWithVersion[V]],
    encoder:           Encoder[Entity[V]],
    valueEncoder:      Encoder[V],
    decoder:           Decoder[Entity[V]],
    dpl:               Decoder[V]
  ): Route = {

    import io.circe.generic.auto._

    implicit val insertRouteLogic = InsertEntityLogic()

    implicit val updateRouteLogic = UpdateEntityLogic()

    implicit val getRouteLogic =
      GetEntityLogic[V]()

    getPostRoute[UpdateReq[V]].route ~
      getPostRoute[InsertReq[V]].route ~
      getPostRoute[GetEntityReq[V]].route
  }

}
