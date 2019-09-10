package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.shared.comm.postRequests.ResetRequest

import scala.concurrent.{ExecutionContextExecutor, Future}

case class ResetServerStateLogic(
)(
  implicit
//  persistentServiceProvider: PersistentServiceProvider,
  contextExecutor:           ExecutionContextExecutor)
    extends RouteLogic[ResetRequest] {
//  implicit val ce: ExecutionContextExecutor = contextExecutor

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
  ): Future[Option[ResetRequest.Res]] = {
    ??? //todo-now
  }

  override def getRouteName: String =
    "debug 319CCAEA2DE4 - reset server logic"
}
