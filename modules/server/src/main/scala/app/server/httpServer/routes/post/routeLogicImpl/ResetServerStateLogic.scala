package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.testRelatedOperations.ResetStateEPOP
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.testRelatedOperations.ResetStateEPOP.ResetStateEPOP
import app.shared.comm.postRequests.ResetServerHTTPReq

import scala.concurrent.{ExecutionContextExecutor, Future}

case class ResetServerStateLogic(
    persistentServiceProvider: PersistentServiceProvider,
    contextExecutor:           ExecutionContextExecutor
) extends RouteLogic[ResetServerHTTPReq] {
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
      param: ResetServerHTTPReq.Par
  ): Future[Option[ResetServerHTTPReq.Res]] = {

    val to: ResetServerHTTPReq.Par => ResetStateEPOP#Par =
      ??? //todo-now

    val executeOp: ResetStateEPOP#Par => Future[ResetStateEPOP#Res] =
      (par: ResetStateEPOP#Par) =>
        persistentServiceProvider
          .executePO[Nothing, ResetStateEPOP](par)

    val h: ResetServerHTTPReq.Par => Future[
      ResetStateEPOP#Res
    ] = to.andThen(executeOp)

    val back: ResetStateEPOP#Res => Option[ResetServerHTTPReq.Res] =
      ???
    // todo-now

    h(param).map(back)

  }
}
