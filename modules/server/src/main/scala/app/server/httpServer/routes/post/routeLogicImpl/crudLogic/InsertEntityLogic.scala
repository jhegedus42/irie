package app.server.httpServer.routes.post.routeLogicImpl.crudLogic

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.postRequests.InsertReq
import app.shared.comm.postRequests.InsertReq.{
  InsertReqPar,
  InsertReqRes
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

case class InsertEntityLogic[V <: EntityValue[V]](
)(
  implicit
  paw:             PersistentActorWhisperer,
  decoderEntityV:  Decoder[Entity[V]],
  encoderEntityV:  Encoder[Entity[V]],
  encoderV:        Encoder[V],
  classTag:        ClassTag[V],
  contextExecutor: ExecutionContextExecutor)
    extends RouteLogic[InsertReq[V]] {

  override def getHttpReqResult(
    param: InsertReqPar[V]
  ): Future[InsertReqRes[V]] = {
    val r: Future[Option[Entity[V]]] =
      paw.insertNewEntity(param.value)
    r.map((o: Option[Entity[V]]) => InsertReqRes(o.get)) // todo-later - some error handling
  }
  override def getRouteName: String =
    "debug 319CCAEA2DE4 - insert logic"

}
