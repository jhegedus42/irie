package app.server.httpServer.routes.post.routeLogicImpl.logic.read

import java.util.Calendar

import app.server.httpServer.routes.post.RouteLogic
import app.shared.comm.postRequests.SumIntRoute
import app.shared.comm.postRequests.SumIntRoute.{SumIntPar, SumIntRes}

import scala.concurrent.Future

/**
  *
  * Type class instance.
  *
  */

object SumIntLogic extends RouteLogic[SumIntRoute] {

  override def getHttpReqResult(
    param: SumIntPar
  ): Future[SumIntRes] = {
    def time = Calendar.getInstance.getTime
    println(
      s"serverside - sum int view typeclass was executed - $time"
    )
    Future.successful(SumIntRes(param.x + param.y))
  }
  override def getRouteName: String =
    "debug 319CCAEA2DE4 - sum int logic"
}
