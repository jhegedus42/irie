package app.shared.comm.postRequests

import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.Entity
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}
import io.circe.generic.JsonCodec

object GetEntityReq {

  import io.circe.generic.auto._
  import io.circe.syntax._
  import io.circe.generic.JsonCodec

  @JsonCodec
  case class Par[V <: EntityValue[V]](
    refToEntityWithoutVersion: RefToEntityWithoutVersion[V])
      extends PostRequest.Parameter

  @JsonCodec
  case class Res[V <: EntityValue[V]](
    optionEntity: Option[Entity[V]])
      extends PostRequest.Result

}

//@JsonCodec
class GetEntityReq[V <: EntityValue[V]] extends PostRequest[ReadRequest] {
  override type ParT     = GetEntityReq.Par[V]
  override type ResT     = GetEntityReq.Res[V]
  override type PayLoadT = V

}
