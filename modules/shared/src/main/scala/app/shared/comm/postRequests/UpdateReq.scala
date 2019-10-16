package app.shared.comm.postRequests

import app.shared.comm.{PostRequest, WriteRequest}
import app.shared.comm.postRequests.UpdateReq.{UpdateReqPar, UpdateReqRes}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType

object UpdateReq {

  case class UpdateReqPar[V <: EntityType[V]](
                                                currentEntity: EntityWithRef[V],
                                                newValue:      V
  ) extends PostRequest.Parameter

  case class UpdateReqRes[V <: EntityType[V]](entity: EntityWithRef[V])
      extends PostRequest.Result
}

class UpdateReq[V <: EntityType[V]] extends PostRequest[WriteRequest] {
  override type ParT     = UpdateReqPar[V]
  override type ResT     = UpdateReqRes[V]
  override type PayLoadT = V

}

