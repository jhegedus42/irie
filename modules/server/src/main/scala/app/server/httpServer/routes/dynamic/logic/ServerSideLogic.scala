package app.server.httpServer.routes.dynamic.logic

import java.util.Calendar

import app.shared.comm.PostRequest
import app.shared.comm.requests.SumIntPostRequest
import app.shared.comm.requests.SumIntPostRequest.{SumIntPar, SumIntRes}

import scala.concurrent.Future

private[dynamic] object ServerSideLogic {

  trait ServerLogicTypeClass[V <: PostRequest] {
    def getResult(param: V#Par ): Future[Option[V#Res]]
  }

  implicit object SumIntViewInstance extends
    ServerLogicTypeClass[SumIntPostRequest] {

    override def getResult(param:SumIntPar ):
      Future[Option[SumIntRes]] = {
        def time=Calendar.getInstance.getTime
        println(s"serverside - sum int view typeclass was executed - $time")
        Future.successful(Some(SumIntRes(param.x+param.y)))
    }
  }



}
