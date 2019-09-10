package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.shared.comm.postRequests.GetEntityReq
import app.shared.comm.postRequests.GetEntityReq.{GetEntityReqPar, GetEntityReqRes}
import app.shared.entity.entityValue.EntityValue
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}

case class GetRL[V <: EntityValue[V]](
//    persistenceModule: PersistentServiceProvider,
//    d:                 Decoder[Entity[V]],
    dv:                 Decoder[V],
    contextExecutor:   ExecutionContextExecutor,
) extends RouteLogic[GetEntityReq[V]] {

  implicit val ce: ExecutionContextExecutor = contextExecutor


  override def getHttpReqResult(
      param: GetEntityReqPar[V]
  ): Future[Option[GetEntityReqRes[V]]] = {
    ??? // todo-now
  }

  override def getRouteName: String = "debug 319CCAEA2DE4 - get route logic"

}
