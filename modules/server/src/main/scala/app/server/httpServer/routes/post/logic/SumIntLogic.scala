package app.server.httpServer.routes.post.logic

import java.util.Calendar

import app.server.httpServer.routes.post.Logic
import app.shared.comm.postRequests.SumIntPostRequest
import app.shared.comm.postRequests.SumIntPostRequest.{SumIntPar, SumIntRes}

import scala.concurrent.Future


object SumIntLogic
  extends Logic[SumIntPostRequest] {

  override def getResult(param:SumIntPar ):
  Future[Option[SumIntRes]] = {
    def time=Calendar.getInstance.getTime
    println(s"serverside - sum int view typeclass was executed - $time")
    Future.successful(Some(SumIntRes(param.x+param.y)))
  }
}
