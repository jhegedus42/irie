package app.server.httpServer.routes.post.routeLogicImpl.crudLogic

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.WriteRequest
import app.shared.comm.postRequests.InsertReq
import app.shared.comm.postRequests.InsertReq.{InsertReqPar, InsertReqRes}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityValue
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

case class InsertEntityLogic[V <: EntityValue[V]](
)(
                                                   implicit
                                                   paw:             PersistentActorWhisperer,
                                                   decoderEntityV:  Decoder[EntityWithRef[V]],
                                                   encoderEntityV:  Encoder[EntityWithRef[V]],
                                                   encoderV:        Encoder[V],
                                                   classTag:        ClassTag[V],
                                                   contextExecutor: ExecutionContextExecutor)
    extends RouteLogic[InsertReq[V]] {

  override def getHttpReqResult(
    param: InsertReqPar[V]
  ): Future[InsertReqRes[V]] = {
    val r: Future[Option[EntityWithRef[V]]] =
      paw.WriteOps.insertNewEntity(param.value)
    r.map((o: Option[EntityWithRef[V]]) => InsertReqRes(o.get)) // todo-later - some error handling
  }
  override def getRouteName: String =
    "debug 319CCAEA2DE4 - insert logic"

}
