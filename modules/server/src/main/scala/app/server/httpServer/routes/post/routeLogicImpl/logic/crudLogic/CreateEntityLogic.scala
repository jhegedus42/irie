package app.server.httpServer.routes.post.routeLogicImpl.logic.crudLogic

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.postRequests.write.CreateEntityReq
import app.shared.comm.postRequests.write.CreateEntityReq.{CreateEntityReqPar, CreateEntityReqRes}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

case class CreateEntityLogic[V <: EntityType[V]](
)(
  implicit
  paw:             PersistentActorWhisperer,
  decoderEntityV:  Decoder[EntityWithRef[V]],
  encoderEntityV:  Encoder[EntityWithRef[V]],
  encoderV:        Encoder[V],
  classTag:        ClassTag[V],
  decoderV:Decoder[V],
  contextExecutor: ExecutionContextExecutor)
    extends RouteLogic[CreateEntityReq[V]] {

  override def getHttpReqResult(
    param: CreateEntityReqPar[V]
  ): Future[CreateEntityReqRes[V]] = {
    val r: Future[Option[EntityWithRef[V]]] =
      paw.WriteOps.insertNewEntity(param.value)
    r.map((o: Option[EntityWithRef[V]]) => CreateEntityReqRes(o.get)) // todo-later - some error handling
  }
  override def getRouteName: String =
    "debug 319CCAEA2DE4 - insert logic"

}
