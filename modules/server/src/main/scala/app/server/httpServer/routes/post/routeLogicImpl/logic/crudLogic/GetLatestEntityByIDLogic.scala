package app.server.httpServer.routes.post.routeLogicImpl.logic.crudLogic

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.postRequests.GetLatestEntityByIDReq
import app.shared.comm.{ReadRequest, WriteRequest}
import app.shared.comm.postRequests.GetLatestEntityByIDReq.{Par, Res}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}

case class GetLatestEntityByIDLogic[V <: EntityType[V]](
)(
  implicit
  paw:             PersistentActorWhisperer,
  dv:              Decoder[V],
  de:              Decoder[EntityWithRef[V]],
  contextExecutor: ExecutionContextExecutor)
    extends RouteLogic[GetLatestEntityByIDReq[V]] {

  override def getHttpReqResult(param: Par[V]): Future[Res[V]] = {

    val res: Future[Option[EntityWithRef[V]]] =
      paw.getEntityWithLatestVersion(param.refToEntityByID)
//    paw.getEntityWithVersion(param.refToEntityWithVersion)

    val res2: Future[Res[V]] = res.map(r => Res(r))
    res2
  }

  override def getRouteName: String =
    "debug 319CCAEA2DE4 - get route logic"

}
