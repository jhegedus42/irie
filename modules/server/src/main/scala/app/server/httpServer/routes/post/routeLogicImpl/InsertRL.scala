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

case class InsertRL[V <: EntityValue[V]](
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

    implicit val executer: InsertEPOP.Executor[V] =
      InsertEPOP.Executor(decoder  = decoderEntityV,
                        encoder  = encoderEntityV,
                        encoderV = encoderV,
                        classTag = classTag)

    val poPar: InsertEPOP.InsertPOPar[V] = InsertEPOP.InsertPOPar(param.value)

    val poRes: Future[InsertEPOP.InsertPORes[V]] =
      persistenceModule.executePO[V, InsertEPOP[V]](poPar)

    def poRes2ReqRes: InsertEPOP.InsertPORes[V] => Option[InsertReqRes[V]] =
      poRes => poRes.res.toOption.map(InsertReqRes(_))

    poRes.map(poRes2ReqRes(_))(contextExecutor)

  }

}
