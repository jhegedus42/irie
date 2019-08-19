package app.shared.comm.requests

import app.shared.comm.Request
import app.shared.comm.requests.GetEntityRequest.{GetEntityRequestParameter, GetEntityRequestResult}
import app.shared.entity.{Entity, RefToEntity}
import app.shared.entity.entityValue.EntityValue

object GetEntityRequest {

  case class GetEntityRequestParameter[V<:EntityValue[V]](par:RefToEntity[V]) extends Request.PostReqParameter

  case class GetEntityRequestResult[V<:EntityValue[V]](res:Option[Entity[V]] ) extends Request.PostReqResult

}

class GetEntityRequest[V<:EntityValue[V]] extends Request {
  type Par = GetEntityRequestParameter[V]
  type Res = GetEntityRequestResult[V]
}