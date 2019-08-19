package app.shared.comm.requests

import app.shared.comm.PostRequest
import app.shared.comm.requests.GetEntityPostRequest.{
  GetEntityRequestParameter,
  GetEntityRequestResult
}
import app.shared.entity.{Entity, RefToEntity}
import app.shared.entity.entityValue.EntityValue

object GetEntityPostRequest {

  case class GetEntityRequestParameter[V <: EntityValue[V]](
      par: RefToEntity[V]
  ) extends PostRequest.Parameter

  case class GetEntityRequestResult[V <: EntityValue[V]](
      res: Option[Entity[V]]
  ) extends PostRequest.Result

}

class GetEntityPostRequest[V <: EntityValue[V]] extends PostRequest {
  type Par = GetEntityRequestParameter[V]
  type Res = GetEntityRequestResult[V]
}
