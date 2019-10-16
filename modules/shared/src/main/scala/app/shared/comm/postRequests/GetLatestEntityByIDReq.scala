package app.shared.comm.postRequests

import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.EntityWithRef
import app.shared.entity.refs.{RefToEntityByID, RefToEntityWithVersion}
import io.circe.generic.JsonCodec

object GetLatestEntityByIDReq {

  import io.circe.generic.auto._
  import io.circe.syntax._
  import io.circe.generic.JsonCodec

  @JsonCodec
  case class Par[V <: EntityType[V]](
    refToEntityByID: RefToEntityByID[V])
      extends PostRequest.Parameter

  @JsonCodec
  case class Res[V <: EntityType[V]](
    optionEntity: Option[EntityWithRef[V]])
      extends PostRequest.Result

}

//@JsonCodec
class GetLatestEntityByIDReq[V <: EntityType[V]]
    extends PostRequest[ReadRequest] {
  override type ParT     = GetLatestEntityByIDReq.Par[V]
  override type ResT     = GetLatestEntityByIDReq.Res[V]
  override type PayLoadT = V

}
