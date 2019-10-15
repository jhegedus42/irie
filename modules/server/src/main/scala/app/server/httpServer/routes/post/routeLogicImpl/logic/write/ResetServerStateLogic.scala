package app.server.httpServer.routes.post.routeLogicImpl.logic.write

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.postRequests.ResetRequest

import scala.concurrent.{ExecutionContextExecutor, Future}

case class ResetServerStateLogic(
)(
  implicit
  paw: PersistentActorWhisperer,
  contextExecutor: ExecutionContextExecutor)
    extends RouteLogic[ResetRequest] {

  /**
    *
    * Calculates/executes what calling a REST endpoint should do.
    *
    * It is used by, for example :
    * app.server.httpServer.routes.post.PostRouteForAkkaHttpFactory#getPostRoute(...) : Route
    *
    * @param param
    * @return
    */
  override def getHttpReqResult(
    param: ResetRequest.Par
  ): Future[ResetRequest.Res] = {
    val r: Future[String] = paw.WriteOps.resetTheState()
    r.map(x => ResetRequest.Res(x))
  }

  override def getRouteName: String =
    "debug 319CCAEA2DE4 - reset server logic"
}
