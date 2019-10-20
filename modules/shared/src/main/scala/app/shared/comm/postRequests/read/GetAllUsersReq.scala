package app.shared.comm.postRequests.read

import app.shared.comm.{PostRequest, ReadRequest}
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
//import io.circe.syntax._
//import io.circe.generic.JsonCodec

import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.EntityWithRef
import app.shared.entity.refs.{
  RefToEntityByID,
  RefToEntityWithVersion
}

import io.circe.generic.JsonCodec


class GetAllUsersReq extends PostRequest[ReadRequest] {

  override type ParT     = GetAllUsersReq.Par
  override type PayLoadT = Unit // has no payload
  override type ResT     = GetAllUsersReq.Res

}

case class AdminPassword(pwd: String)

object GetAllUsersReq {
  import io.circe.generic.auto._
  import io.circe.syntax._
  import io.circe.generic.JsonCodec

  @JsonCodec
  case class Par(adminPassword: AdminPassword)
      extends PostRequest.Parameter

  @JsonCodec
  case class Res(allUserRefs: List[RefToEntityWithVersion[User]])
      extends PostRequest.Result

}
