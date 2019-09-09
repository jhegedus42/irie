package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.InsertEPOP
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
import scala.util.Try

case class InsertRL[V <: EntityValue[V]]()(
    implicit
    persistenceModule: PersistentServiceProvider,
    decoderEntityV:    Decoder[Entity[V]],
    encoderEntityV:    Encoder[Entity[V]],
    encoderV:          Encoder[V],
    classTag:          ClassTag[V],
    contextExecutor:   ExecutionContextExecutor
) extends RouteLogic[InsertReq[V]] {

  override def getHttpReqResult(
      param: InsertReqPar[V]
  ): Future[Option[InsertReqRes[V]]] = {

    implicit val executor: InsertEPOP.Executor[V] =
      InsertEPOP.Executor()

    persistenceModule
      .executePO[V, InsertEPOP[V]](
        InsertEPOP.InsertPOPar(param.value)
      )
      .map(poRes => poRes.res.toOption.map(InsertReqRes(_)))(
        executor = contextExecutor
      )

  }
  override def getRouteName: String = "debug 319CCAEA2DE4 - insert logic"

}
