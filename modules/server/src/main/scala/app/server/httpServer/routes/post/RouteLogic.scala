package app.server.httpServer.routes.post

import app.server.httpServer.routes.post.routeLogicImpl.SumIntRL
import app.shared.comm.PostRequest

import scala.concurrent.Future


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
  def getHttpReqResult(param: Req#Par): Future[Option[Req#Res]]

}

object RouteLogic {
  implicit val sumIntInstance = SumIntRL
}
