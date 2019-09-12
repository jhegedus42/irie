package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.InsertReq.{
  InsertReqPar,
  InsertReqRes
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue

object InsertReq {
  case class InsertReqPar[V <: EntityValue[V]](value: V)
      extends PostRequest.Parameter

  case class InsertReqRes[V <: EntityValue[V]](entity: Entity[V])
      extends PostRequest.Result
}

class InsertReq[V <: EntityValue[V]] extends PostRequest {
  override type Par     = InsertReqPar[V]
  override type Res     = InsertReqRes[V]
  override type PayLoad = V

}