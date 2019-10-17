package app.shared.comm.postRequests

import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.EntityWithRef
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.generic.JsonCodec
import app.shared.utils.UUID_Utils.EntityIdentity

object LoginReq {
  import io.circe.generic.auto._
  import io.circe.syntax._
  import io.circe.generic.JsonCodec

  @JsonCodec
  case class Par(
    userName: String,
    password: String)
      extends PostRequest.Parameter

  @JsonCodec
  case class Res(optionEntityIdentity: Option[EntityIdentity])
      extends PostRequest.Result
}

//@JsonCodec
class LoginReq extends PostRequest[ReadRequest] {
  override type ParT     = LoginReq.Par
  override type ResT     = LoginReq.Res
  override type PayLoadT = Unit
}
