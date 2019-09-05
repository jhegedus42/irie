package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.InsertPO
import app.shared.comm.postRequests.InsertNewEntityRoute
import app.shared.comm.postRequests.InsertNewEntityRoute.{
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
    d:                 Decoder[Entity[V]],
    e:                 Encoder[Entity[V]],
    ct:                ClassTag[V],
    contextExecutor:   ExecutionContextExecutor
) extends RouteLogic[InsertNewEntityRoute[V]] {

  override def getHttpReqResult(
      param: InsertReqPar[V]
  ): Future[Option[InsertReqRes[V]]] = {

    implicit val executer: InsertPO.Executor[V] =
      InsertPO.Executor(d, e,ct)

    val poPar: InsertPO.InsertPOPar[V] = InsertPO.InsertPOPar(param.value)

    val poRes: Future[InsertPO.InsertPORes[V]] =
      persistenceModule.executePO[V,InsertPO[V]](poPar)

    def poRes2ReqRes : InsertPO.InsertPORes[V] => Option[InsertReqRes[V]] =
       poRes => poRes.res.toOption.map(InsertReqRes(_))

      poRes.map(poRes2ReqRes(_))(contextExecutor)

  }

}

