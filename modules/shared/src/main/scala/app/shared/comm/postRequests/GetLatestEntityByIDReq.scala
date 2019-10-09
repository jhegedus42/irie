package app.shared.comm.postRequests

import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.EntityWithRef
import app.shared.entity.refs.{RefToEntityWithVersion}
import io.circe.generic.JsonCodec

object GetLatestEntityByIDReq {

  import io.circe.generic.auto._
  import io.circe.syntax._
  import io.circe.generic.JsonCodec

  @JsonCodec
  case class Par[V <: EntityValue[V]](
    refToEntityWithVersion: RefToEntityWithVersion[V])
      extends PostRequest.Parameter

  @JsonCodec
  case class Res[V <: EntityValue[V]](
    optionEntity: Option[EntityWithRef[V]])
      extends PostRequest.Result

}

//@JsonCodec
class GetLatestEntityByIDReq[V <: EntityValue[V]]
    extends PostRequest[ReadRequest] {
  override type ParT     = GetLatestEntityByIDReq.Par[V]
  override type ResT     = GetLatestEntityByIDReq.Res[V]
  override type PayLoadT = V

}
