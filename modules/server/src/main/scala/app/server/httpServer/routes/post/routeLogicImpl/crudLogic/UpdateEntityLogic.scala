package app.server.httpServer.routes.post.routeLogicImpl.crudLogic

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.postRequests.UpdateReq
import app.shared.comm.postRequests.UpdateReq.{
  UpdateReqPar,
  UpdateReqRes
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

case class UpdateEntityLogic[V <: EntityValue[V]](
)(
  implicit
  paw:             PersistentActorWhisperer,
  decoderEntityV:  Decoder[Entity[V]],
  encoderEntityV:  Encoder[Entity[V]],
  _encoderV:       Encoder[V],
  classTag:        ClassTag[V],
  contextExecutor: ExecutionContextExecutor)
    extends RouteLogic[UpdateReq[V]] {

  override def getHttpReqResult(
    param: UpdateReqPar[V]
  ): Future[UpdateReqRes[V]] = {

    val r: Future[Option[Entity[V]]] =
      paw.WriteOps.updateEntity(param.currentEntity, param.newValue)
    r.map((x: Option[Entity[V]]) => UpdateReqRes(x.get))

  }

  /**
    * This is used for debugging.
    *
    * @return
    */
  override def getRouteName: String =
    "debug 319CCAEA2DE4 - update logic"
}