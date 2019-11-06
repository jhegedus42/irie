package app.shared.comm.postRequests.read

import app.shared.comm.{PostRequest, ReadRequest}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.generic.JsonCodec

object GetEntityReq {
  import io.circe.generic.auto._
  import io.circe.syntax._
  import io.circe.generic.JsonCodec

  @JsonCodec
  case class Par[V <: EntityType[V]](
    refToEntityWithVersion: RefToEntityWithVersion[V])
      extends PostRequest.Parameter

  @JsonCodec
  case class Res[V <: EntityType[V]](
    optionEntity: Option[EntityWithRef[V]])
      extends PostRequest.Result
}

//@JsonCodec
class GetEntityReq[V <: EntityType[V]]
    extends PostRequest[ReadRequest] with ReadRequest {
  override type ParT     = GetEntityReq.Par[V]
  override type ResT     = GetEntityReq.Res[V]
  override type PayLoadT = V

}
