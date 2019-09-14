package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetEntityReq.{
  Par,
  GetEntityReqRes
}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.Entity
import app.shared.entity.refs.{
  RefToEntityWithVersion,
  RefToEntityWithoutVersion
}

object GetEntityReq {

  case class Par[V <: EntityValue[V]](
    refToEntityWithoutVersion: RefToEntityWithoutVersion[V])
      extends PostRequest.Parameter

  case class GetEntityReqRes[V <: EntityValue[V]](
    optionEntity: Option[Entity[V]])
      extends PostRequest.Result

}

class GetEntityReq[V <: EntityValue[V]]
    extends PostRequest {
  override type Par     = Par[V]
  override type Res     = GetEntityReqRes[V]
  override type PayLoad = V
}
