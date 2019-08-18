package app.server.httpServer.routes.logic

import java.util.Calendar

import app.shared.comm.views.PostRequest
import app.shared.dataModel.views.SumIntPostRequest
import app.shared.dataModel.views.SumIntPostRequest.{SumIntView_Par, SumIntView_Res}

private[routes] object ServerSideLogic {

  trait ServerLogicTypeClass[V <: PostRequest] {
    def getView(param: V#Par ): Option[V#Res]
  }

  implicit object ServerLogicTypeClassImpl_SumIntView extends ServerLogicTypeClass[SumIntPostRequest] {
    override def getView(param:SumIntView_Par ): Option[SumIntView_Res] = {

      def time=Calendar.getInstance.getTime
      println(s"serverside - sum int view typeclass was executed - $time")
      Some(SumIntView_Res(param.x+param.y))
    }
  }




}
