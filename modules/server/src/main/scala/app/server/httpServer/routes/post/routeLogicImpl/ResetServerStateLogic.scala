package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.testRelatedOperations.Reset
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.testRelatedOperations.Reset.ResetStateEPOP
import app.shared.comm.postRequests.ResetRequest
import app.shared.comm.postRequests.ResetRequest.Res

import scala.concurrent.{ExecutionContextExecutor, Future}

case class ResetServerStateLogic(
)(
  implicit
  persistentServiceProvider: PersistentServiceProvider,
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

    val to = (x: ResetRequest.Par) =>
      Reset.ResetStateEPOP.ResetEPOPPar()

    val executeOp =
      (par: ResetStateEPOP#Par) =>
        persistentServiceProvider
          .executePO[Nothing, ResetStateEPOP](par)

    val h = to.andThen(executeOp)

    val back =
      (x: ResetStateEPOP.ResetEPOPRes) =>
        Option(Res("minden shiraly!"))

    h(param).map(back)

  }

  override def getRouteName: String =
    "debug 319CCAEA2DE4 - reset server logic"
}
