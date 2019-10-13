package app.shared.comm.postRequests

import app.shared.comm.{PostRequest, WriteRequest}
import app.shared.comm.postRequests.CreateEntityReq.{CreateEntityReqPar, CreateEntityReqRes}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityValue

object CreateEntityReq {
  case class CreateEntityReqPar[V <: EntityValue[V]](value: V)
      extends PostRequest.Parameter

  case class CreateEntityReqRes[V <: EntityValue[V]](entity: EntityWithRef[V])
      extends PostRequest.Result
}

class CreateEntityReq[V <: EntityValue[V]] extends PostRequest[WriteRequest] {
  override type ParT     = CreateEntityReqPar[V]
  override type ResT     = CreateEntityReqRes[V]
  override type PayLoadT = V

}
