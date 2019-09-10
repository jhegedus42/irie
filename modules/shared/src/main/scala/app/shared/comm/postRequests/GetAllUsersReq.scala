package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetAllUsersReq.Password
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User

class GetAllUsersReq extends PostRequest {

  // todo-now - implement this
  // this has a "password"

  override type Par     = GetAllUsersReq.Password
  override type PayLoad = Unit // has no payload
  override type Res     = GetAllUsersReq.AllUsers

}

object GetAllUsersReq {

  case class Password(pwd: String) extends PostRequest.Parameter

  case class AllUsers(allUsers: List[Entity[User]])
      extends PostRequest.Result

}
