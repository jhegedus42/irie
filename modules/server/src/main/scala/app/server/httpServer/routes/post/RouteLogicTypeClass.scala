package app.server.httpServer.routes.post

import app.server.httpServer.routes.post.routeLogicImpl.SumIntRouteLogicTCInst
import app.shared.comm.PostRouteType

import scala.concurrent.Future

/**
  *
  * TC at the end symbolizes : TypeClass
  *
  * @tparam Req
  */
trait RouteLogicTypeClass[Req <: PostRouteType] {
  // todo-later
  //   this is where we should put the OCC
  //   because this is a composite operation
  //   for example the transaction example (Alice
  //   to Bob and then Alice to Cica) consists of
  //   two atomic parts : read and write
  //   they are two atomic persistente Operations
  //
  def getResult(param: Req#Par): Future[Option[Req#Res]]
}

object RouteLogicTypeClass {
  implicit val sumIntInstance = SumIntRouteLogicTCInst
}
