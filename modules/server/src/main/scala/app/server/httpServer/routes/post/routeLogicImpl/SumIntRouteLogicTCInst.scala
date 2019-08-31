package app.server.httpServer.routes.post.routeLogicImpl

import java.util.Calendar

import app.server.httpServer.routes.post.RouteLogicTypeClass
import app.shared.comm.postRequests.SumIntRoute
import app.shared.comm.postRequests.SumIntRoute.{SumIntPar, SumIntRes}

import scala.concurrent.Future


object SumIntRouteLogicTCInst
  extends RouteLogicTypeClass[SumIntRoute] {

  override def getResult(param:SumIntPar ):
  Future[Option[SumIntRes]] = {
    def time=Calendar.getInstance.getTime
    println(s"serverside - sum int view typeclass was executed - $time")
    Future.successful(Some(SumIntRes(param.x+param.y)))
  }
}
