package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.UpdateEntityRoute.{
  UpdateReqPar,
  UpdateReqRes
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue

object UpdateEntityRoute {
  case class UpdateReqPar[V <: EntityValue[V]](
      currentEntity: Entity[V],
      newValue:      V
  ) extends PostRequest.Parameter

  case class UpdateReqRes[V <: EntityValue[V]](entity: Entity[V])
      extends PostRequest.Result
}

class UpdateEntityRoute[V <: EntityValue[V]] extends PostRequest {
  override type Par     = UpdateReqPar[V]
  override type Res     = UpdateReqRes[V]
  override type PayLoad = V

}
