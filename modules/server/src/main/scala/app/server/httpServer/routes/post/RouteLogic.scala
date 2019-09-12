package app.server.httpServer.routes.post

import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.server.httpServer.routes.post.routeLogicImpl.{
  GetAllUsersLogic,
  ResetServerStateLogic,
  SumIntLogic
}
import app.shared.comm.PostRequest

import scala.concurrent.{ExecutionContextExecutor, Future}

/**
  *
  * This is a typeclass.
  *
  * @tparam Req
  */
trait RouteLogic[Req <: PostRequest] {

  // todo-later
  //   this is where we should put the OCC
  //   because this is a composite operation
  //   for example the transaction example (Alice
  //   to Bob and then Alice to Cica) consists of
  //   two atomic parts : read and write
  //   they are two atomic persistente Operations
  //

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
  def getHttpReqResult(param: Req#Par): Future[Req#Res]

  /**
    * This is used for debugging.
    * @return
    */
  def getRouteName: String

}

object RouteLogic {
  implicit val sumIntInstance = SumIntLogic

  implicit def getResetLogic(
    implicit paw:    PersistentActorWhisperer,
    contextExecutor: ExecutionContextExecutor
  ): ResetServerStateLogic = ResetServerStateLogic()

  implicit def getAllUsersLogic(
    implicit paw:    PersistentActorWhisperer,
    contextExecutor: ExecutionContextExecutor
  ): GetAllUsersLogic = GetAllUsersLogic()

}
