package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.testRelatedOperations.ResetStateEPOP
import app.shared.comm.postRequests.ResetServerReq

import scala.concurrent.{ExecutionContextExecutor, Future}

case class ResetServerStateLogic(
    persistentServiceProvider: PersistentServiceProvider,
    contextExecutor:           ExecutionContextExecutor
) extends RouteLogic[ResetServerReq] {
  implicit val ce: ExecutionContextExecutor = contextExecutor

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
      param: ResetServerReq.ResetServerReqPar
  ): Future[Option[ResetServerReq.ResetServerReqRes]] =
    persistentServiceProvider.executePO[Nothing,ResetStateEPOP](param)
}
