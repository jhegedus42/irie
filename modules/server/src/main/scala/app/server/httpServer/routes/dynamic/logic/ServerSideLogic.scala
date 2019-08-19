package app.server.httpServer.routes.dynamic.logic

import java.util.Calendar

import app.shared.comm.Request
import app.shared.comm.requests.SumIntPostRequest
import app.shared.comm.requests.SumIntPostRequest.{SumIntView_Par, SumIntView_Res}

import scala.concurrent.Future

private[dynamic] object ServerSideLogic {

  trait ServerLogicTypeClass[V <: Request] {
    def getResult(param: V#Par ): Future[Option[V#Res]]
  }

  implicit object SumIntViewInstance extends
    ServerLogicTypeClass[SumIntPostRequest] {

    override def getResult(param:SumIntView_Par ):
      Future[Option[SumIntView_Res]] = {
        def time=Calendar.getInstance.getTime
        println(s"serverside - sum int view typeclass was executed - $time")
        Future.successful(Some(SumIntView_Res(param.x+param.y)))
    }
  }



}
