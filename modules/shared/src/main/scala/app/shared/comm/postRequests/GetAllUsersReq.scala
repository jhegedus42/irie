package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetAllUsersReq.Password
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.{
  RefToEntityWithVersion,
  RefToEntityWithoutVersion
}

class GetAllUsersReq extends PostRequest {


  override type Par     = GetAllUsersReq.Password
  override type PayLoad = Unit // has no payload
  override type Res     = GetAllUsersReq.AllUsers

}

case class AdminPassword(pwd: String)

object GetAllUsersReq {

  case class Password(adminPassword: AdminPassword)
      extends PostRequest.Parameter

  case class AllUsers(allUserRefs: List[RefToEntityWithoutVersion[User]])
      extends PostRequest.Result

}
