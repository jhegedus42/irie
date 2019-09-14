package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.UpdateReq.{
  UpdateReqPar,
  UpdateReqRes
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue

object UpdateReq {

  case class UpdateReqPar[V <: EntityValue[V]](
      currentEntity: Entity[V],
      newValue:      V
  ) extends PostRequest.Parameter

  case class UpdateReqRes[V <: EntityValue[V]](entity: Entity[V])
      extends PostRequest.Result
}

class UpdateReq[V <: EntityValue[V]] extends PostRequest {
  override type ParT     = UpdateReqPar[V]
  override type ResT     = UpdateReqRes[V]
  override type PayLoadT = V

}
