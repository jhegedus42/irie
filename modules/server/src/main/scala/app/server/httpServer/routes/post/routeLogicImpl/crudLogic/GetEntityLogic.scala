package app.server.httpServer.routes.post.routeLogicImpl.crudLogic

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.{ReadRequest, WriteRequest}
import app.shared.comm.postRequests.GetEntityReq
import app.shared.comm.postRequests.GetEntityReq.{Par, Res}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}

case class GetEntityLogic[V <: EntityValue[V]](
)(
  implicit
  paw:             PersistentActorWhisperer,
  dv:              Decoder[V],
  contextExecutor: ExecutionContextExecutor)
    extends RouteLogic[GetEntityReq[V]] {

  override def getHttpReqResult(
    param: Par[V]
  ): Future[Res[V]] = {

    val res: Future[Option[Entity[V]]] =
      paw.getEntityWithLatestVersion(param.refToEntityWithoutVersion)

    val res2: Future[Res[V]] =
      res.map(r => Res(r))
    res2
  }

  override def getRouteName: String =
    "debug 319CCAEA2DE4 - get route logic"

}
