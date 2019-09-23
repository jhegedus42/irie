package app.shared.comm.postRequests

import app.shared.comm.{PostRequest, WriteRequest}
import app.shared.comm.postRequests.InsertReq.{InsertReqPar, InsertReqRes}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue

object InsertReq {
  case class InsertReqPar[V <: EntityValue[V]](value: V)
      extends PostRequest.Parameter

  case class InsertReqRes[V <: EntityValue[V]](entity: Entity[V])
      extends PostRequest.Result
}

class InsertReq[V <: EntityValue[V]] extends PostRequest[WriteRequest] {
  override type ParT     = InsertReqPar[V]
  override type ResT     = InsertReqRes[V]
  override type PayLoadT = V

}
