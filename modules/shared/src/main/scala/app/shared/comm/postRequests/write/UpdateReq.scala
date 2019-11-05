package app.shared.comm.postRequests.write

import app.shared.comm.postRequests.write.UpdateReq.{UpdateReqPar, UpdateReqRes}
import app.shared.comm.{PostRequest, WriteRequest}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import io.circe.generic.JsonCodec

object UpdateReq {

  @JsonCodec
  case class UpdateReqPar[V <: EntityType[V]](
    currentEntity: EntityWithRef[V],
    newValue:      V)
      extends PostRequest.Parameter

  @JsonCodec
  case class UpdateReqRes[V <: EntityType[V]](
    entity: EntityWithRef[V])
      extends PostRequest.Result
}

@JsonCodec
case class UpdateReq[V <: EntityType[V]]()
    extends PostRequest[WriteRequest]
    with WriteRequest {
  override type ParT     = UpdateReqPar[V]
  override type ResT     = UpdateReqRes[V]
  override type PayLoadT = V

}
