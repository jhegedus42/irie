package app.server.httpServer.routes.post

import app.server.httpServer.routes.post.routeLogicImpl.SumIntRouteLogic$
import app.shared.comm.PostRequest

import scala.concurrent.Future

/**
  *
  * TC at the end symbolizes : TypeClass
  *
  * @tparam Req
  */
trait RouteLogic[Req <: PostRequest] {
  def getResult(param: Req#Par): Future[Option[Req#Res]]
}

object RouteLogic {
  implicit val sumIntInstance = SumIntRouteLogic$
}
