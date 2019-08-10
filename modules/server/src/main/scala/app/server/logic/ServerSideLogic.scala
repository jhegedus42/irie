package app.server.logic

import app.shared.dataModel.views.SumIntView_HolderObject.{SumIntView, SumIntView_Par, SumIntView_Res}
import app.shared.comm.views.View

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
