package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetAllUsersReq.Par
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}
import io.circe.generic.auto._
//import io.circe.syntax._
import io.circe.generic.JsonCodec


class GetAllUsersReq extends PostRequest {

  override type ParT     = GetAllUsersReq.Par
  override type PayLoadT = Unit // has no payload
  override type ResT     = GetAllUsersReq.Res

}

case class AdminPassword(pwd: String)

object GetAllUsersReq {


  @JsonCodec
  case class Par(adminPassword: AdminPassword)
      extends PostRequest.Parameter

  @JsonCodec
  case class Res(allUserRefs: List[RefToEntityWithoutVersion[User]])
      extends PostRequest.Result

}
