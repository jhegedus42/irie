package app.server.httpServer.routes.dynamic.logic

import java.util.Calendar

import app.shared.comm.requests.Request
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.views.GetEntityRequest.{GetEntityRequestParameter, GetEntityRequestResult}
import app.shared.dataModel.views.{GetEntityRequest, SumIntPostRequest}
import app.shared.dataModel.views.SumIntPostRequest.{SumIntView_Par, SumIntView_Res}
import scalaz.Alpha.V

private[routes] object ServerSideLogic {

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
