package app.server.httpServer.routes.post.routeLogicImpl
import app.server.httpServer.routes.post.RouteLogic
import app.shared.comm.postRequests.UpdateReq
import app.shared.comm.postRequests.UpdateReq.{UpdateReqPar, UpdateReqRes}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

case class UpdateRL[V <: EntityValue[V]]()(
    implicit
//    persistenceModule: PersistentServiceProvider,
    decoderEntityV:    Decoder[Entity[V]],
    encoderEntityV:    Encoder[Entity[V]],
    _encoderV:         Encoder[V],
    classTag:          ClassTag[V],
    contextExecutor:   ExecutionContextExecutor
) extends RouteLogic[UpdateReq[V]] {

  override def getHttpReqResult(
      param: UpdateReqPar[V]
  ): Future[Option[UpdateReqRes[V]]] = {

    ??? //todo-now
  }

  /**
    * This is used for debugging.
    *
    * @return
    */
  override def getRouteName: String = "debug 319CCAEA2DE4 - update logic"
}
