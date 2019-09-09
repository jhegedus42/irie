package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.testRelatedOperations.Reset
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.testRelatedOperations.Reset.ResetStateEPOP
import app.shared.comm.postRequests.ResetServerHTTPReq
import app.shared.comm.postRequests.ResetServerHTTPReq.Res

import scala.concurrent.{ExecutionContextExecutor, Future}

case class ResetServerStateLogic(
)(
  implicit
  persistentServiceProvider: PersistentServiceProvider,
  contextExecutor:           ExecutionContextExecutor)
    extends RouteLogic[ResetServerHTTPReq] {
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
    param: ResetServerHTTPReq.Par
  ): Future[Option[ResetServerHTTPReq.Res]] = {

    val to = (x: ResetServerHTTPReq.Par) =>
      Reset.ResetStateEPOP.ResetEPOPPar()

    val executeOp =
      (par: ResetStateEPOP#Par) =>
        persistentServiceProvider
          .executePO[Nothing, ResetStateEPOP](par)

    val h = to.andThen(executeOp)

    val back =
      (x: ResetStateEPOP.ResetEPOPRes) =>
        Option(Res("greetings from getHttpReqResult"))

    h(param).map(back)

  }
}
