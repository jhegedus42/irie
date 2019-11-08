package app.shared.comm.postRequests.write

import app.shared.comm.postRequests.write.CreateEntityReq.{CreateEntityReqPar, CreateEntityReqRes}
import app.shared.comm.{PostRequest, WriteRequest}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import io.circe.generic.JsonCodec

object CreateEntityReq {
  @JsonCodec
  case class CreateEntityReqPar[V <: EntityType[V]](value: V)
      extends PostRequest.Parameter

  @JsonCodec
  case class CreateEntityReqRes[V <: EntityType[V]](entity: EntityWithRef[V])
      extends PostRequest.Result
}

class CreateEntityReq[V <: EntityType[V]] extends PostRequest[WriteRequest] with WriteRequest {
  override type ParT     = CreateEntityReqPar[V]
  override type ResT     = CreateEntityReqRes[V]
  override type PayLoadT = V

}