package app.server.httpServer.routes

import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.PostRouteFactory.getPostRoute
import app.shared.comm.postRequests.{GetEntityReq, GetLatestEntityByIDReq, InsertReq, UpdateReq}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.routeLogicImpl.crudLogic.{GetEntityLogic, GetLatestEntityByIDLogic, InsertEntityLogic, UpdateEntityLogic}
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.{ReadRequest, WriteRequest}

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
    encoder:           Encoder[EntityWithRef[V]],
    valueEncoder:      Encoder[V],
    decoder:           Decoder[EntityWithRef[V]],
    dpl:               Decoder[V]
  ): Route = {

    import io.circe.generic.auto._

    implicit val insertRouteLogic: InsertEntityLogic[V] =
      InsertEntityLogic()

    implicit val updateRouteLogic: UpdateEntityLogic[V] =
      UpdateEntityLogic()

    implicit val getRouteLogic: GetEntityLogic[V] =
      GetEntityLogic[V]()

    implicit val l4 : GetLatestEntityByIDLogic[V] =
      GetLatestEntityByIDLogic[V]()

    getPostRoute[UpdateReq[V]].route ~
      getPostRoute[GetLatestEntityByIDReq[V]].route ~
      getPostRoute[InsertReq[V]].route ~
      getPostRoute[GetEntityReq[V]].route
  }

}
