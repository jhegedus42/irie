package app.server.httpServer.routes.dynamic.logic.typeClassInstances

import app.server.httpServer.routes.dynamic.logic.ServerSideLogic.ServerLogicTypeClass
import app.shared.comm.requests.GetEntityRequest
import app.shared.comm.requests.GetEntityRequest.{GetEntityRequestParameter, GetEntityRequestResult}
import app.shared.entity.entityValue.EntityValue

private[logic] object GetEntity {


  case class GetEntityLogic[V<:EntityValue[V]]() extends ServerLogicTypeClass[GetEntityRequest[V]] {
    override def getResult(param:GetEntityRequestParameter[V] ): Option[GetEntityRequestResult[V]] = {

      ???
    }
  }

}
