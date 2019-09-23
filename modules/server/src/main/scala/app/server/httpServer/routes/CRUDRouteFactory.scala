package app.server.httpServer.routes

import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.PostRouteFactory.getPostRoute
import app.shared.comm.postRequests.{GetEntityReq, InsertReq, UpdateReq}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.routeLogicImpl.crudLogic.{GetEntityLogic, InsertEntityLogic, UpdateEntityLogic}
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
    encoder:           Encoder[Entity[V]],
    valueEncoder:      Encoder[V],
    decoder:           Decoder[Entity[V]],
    dpl:               Decoder[V]
  ): Route = {

    import io.circe.generic.auto._

    implicit val insertRouteLogic: InsertEntityLogic[V] = InsertEntityLogic()

    implicit val updateRouteLogic: UpdateEntityLogic[V] = UpdateEntityLogic()

    implicit val getRouteLogic: GetEntityLogic[V] =
      GetEntityLogic[V]()

    val r1=
      getPostRoute[UpdateReq[V]].route

//      r1 ~ // todo-now - fix this
//      getPostRoute[WriteRequest, InsertReq[V]].route ~
//      getPostRoute[ReadRequest, GetEntityReq[V]].route
    r1
  }

}
