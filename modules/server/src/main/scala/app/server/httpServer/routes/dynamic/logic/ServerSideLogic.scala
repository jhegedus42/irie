package app.server.httpServer.routes.dynamic.logic

import java.util.Calendar

import app.shared.comm.Request
import app.shared.comm.requests.SumIntPostRequest
import app.shared.comm.requests.SumIntPostRequest.{SumIntView_Par, SumIntView_Res}

private[dynamic] object ServerSideLogic {

  trait ServerLogicTypeClass[V <: Request] {
    def getResult(param: V#Par ): Option[V#Res]
  }

  implicit object SumIntViewInstance extends ServerLogicTypeClass[SumIntPostRequest] {
    override def getResult(param:SumIntView_Par ): Option[SumIntView_Res] = {

      def time=Calendar.getInstance.getTime
      println(s"serverside - sum int view typeclass was executed - $time")
      Some(SumIntView_Res(param.x+param.y))
    }
  }



}
