package app.server.httpServer.routes.dynamic.logic.typeClassInstances

import app.server.httpServer.routes.dynamic.logic.ServerSideLogic.ServerLogicTypeClass
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.views.GetEntityRequest
import app.shared.dataModel.views.GetEntityRequest.{GetEntityRequestParameter, GetEntityRequestResult}

private[logic] object GetEntity {


  case class GetEntityLogic[V<:EntityValue[V]]() extends ServerLogicTypeClass[GetEntityRequest[V]] {
    override def getResult(param:GetEntityRequestParameter[V] ): Option[GetEntityRequestResult[V]] = {

      ???
    }
  }

}
