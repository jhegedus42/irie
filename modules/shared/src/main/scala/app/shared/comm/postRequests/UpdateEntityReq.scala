package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.UpdateEntityReq.{UpdateReqPar, UpdateReqRes}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue

object UpdateEntityReq {
  case class UpdateReqPar[V <: EntityValue[V]]( entity: Entity[V] ) extends PostRequest.Parameter

  case class UpdateReqRes[V <: EntityValue[V]]( entity: Entity[V] ) extends PostRequest.Result
}

class UpdateEntityReq[V<:EntityValue[V]] extends PostRequest {
  override type Par = UpdateReqPar[V]
  override type Res = UpdateReqRes[V]
  override type PayLoad = V

}
