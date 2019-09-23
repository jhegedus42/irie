package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.ReadRequest
import app.shared.comm.postRequests.GetAllUsersReq
import app.shared.entity.entityValue.EntityValue

import scala.concurrent.{ExecutionContextExecutor, Future}


case class GetAllUsersLogic(
)(
  implicit
  paw:             PersistentActorWhisperer,
  contextExecutor: ExecutionContextExecutor
)
    extends RouteLogic[GetAllUsersReq] {

  override def getHttpReqResult(
    param: GetAllUsersReq.Par
  ): Future[GetAllUsersReq.Res] = {
    paw.getAllUserRefs.map(GetAllUsersReq.Res(_))
  }

  override def getRouteName: String =
    "debug 319CCAEA2DE4 - get route logic"

}

object GetAllUsersLogic {

//  implicit def getLogic : GetAllUsersLogic = ???

}
