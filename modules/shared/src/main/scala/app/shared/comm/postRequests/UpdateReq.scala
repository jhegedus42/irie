package app.shared.comm.postRequests

import app.shared.comm.{PostRequest, WriteRequest}
import app.shared.comm.postRequests.UpdateReq.{UpdateReqPar, UpdateReqRes}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityValue

object UpdateReq {

  case class UpdateReqPar[V <: EntityValue[V]](
                                                currentEntity: EntityWithRef[V],
                                                newValue:      V
  ) extends PostRequest.Parameter

  case class UpdateReqRes[V <: EntityValue[V]](entity: EntityWithRef[V])
      extends PostRequest.Result
}

class UpdateReq[V <: EntityValue[V]] extends PostRequest[WriteRequest] {
  override type ParT     = UpdateReqPar[V]
  override type ResT     = UpdateReqRes[V]
  override type PayLoadT = V

}

