package app.server.logic

import app.shared.comm.views.View
import app.shared.dataModel.views.SumIntView
import app.shared.dataModel.views.SumIntView.{SumIntView_Par, SumIntView_Res}

object ServerSideLogic {

  trait ServerLogicTypeClass[V <: View] {
    def getView(param: V#Par ): Option[V#Res]
  }

  implicit object ServerLogicTypeClassImpl_SumIntView extends ServerLogicTypeClass[SumIntView] {
    override def getView(param:SumIntView_Par ): Option[SumIntView_Res] = {
      Some(SumIntView_Res(param.x+param.y))
    }
  }

}
