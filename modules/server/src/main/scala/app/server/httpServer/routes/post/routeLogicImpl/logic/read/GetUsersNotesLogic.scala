package app.server.httpServer.routes.post.routeLogicImpl.logic.read

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.postRequests.GetUsersNotesReq
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.Note

import scala.concurrent.{ExecutionContextExecutor, Future}

case class GetUsersNotesLogic(
)(
  implicit
  paw:             PersistentActorWhisperer,
  contextExecutor: ExecutionContextExecutor)
    extends RouteLogic[GetUsersNotesReq] {

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
    param: GetUsersNotesReq.Par
  ): Future[GetUsersNotesReq.Res] = {

    val p = param.userID.uuid
    def g(
      inOpt: Option[Set[EntityWithRef[Note]]]
    ): GetUsersNotesReq.Res = {
      val in = inOpt.get
      val res =
        in.filter(r => r.entityValue.owner.entityIdentity.uuid == p)
      val res2 = res.map(_.toRef)
      GetUsersNotesReq.Res(Some(res2))
    }
    paw.getNewestEntitiesWithGivenEntityType[Note].map(g(_))
  }

  /**
    * This is used for debugging.
    *
    * @return
    */
  override def getRouteName: String = "get users notes logic"

}
