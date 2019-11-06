package app.server.httpServer.routes.post

import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.server.httpServer.routes.post.routeLogicImpl.logic.read.{GetAllUsersLogic, GetUsersNotesLogic, LoginLogic}
import app.server.httpServer.routes.post.routeLogicImpl.logic.write.ResetServerStateLogic
import app.shared.comm.postRequests.read.GetUsersNotesReq
import app.shared.comm.{PostRequest, PostRequestType}

import scala.concurrent.{ExecutionContextExecutor, Future}

/**
  *
  * This is a typeclass.
  *
  * @tparam Req
  */
trait RouteLogic[Req <: PostRequest[_]] {

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
  def getHttpReqResult(param: Req#ParT): Future[Req#ResT]

  /**
    * This is used for debugging.
    * @return
    */
  def getRouteName: String

}

object RouteLogic {

  implicit def getResetLogic(
    implicit paw:    PersistentActorWhisperer,
    contextExecutor: ExecutionContextExecutor
  ): ResetServerStateLogic = ResetServerStateLogic()

  implicit def getAllUsersLogic(
    implicit paw:    PersistentActorWhisperer,
    contextExecutor: ExecutionContextExecutor
  ): GetAllUsersLogic = GetAllUsersLogic()

  implicit def getLoginLogic(
    implicit
    paw:             PersistentActorWhisperer,
    contextExecutor: ExecutionContextExecutor
  ): LoginLogic = LoginLogic()

  implicit def getUsersNoteLogic(
    implicit
    paw:             PersistentActorWhisperer,
    contextExecutor: ExecutionContextExecutor
  ): GetUsersNotesLogic = GetUsersNotesLogic()

}
