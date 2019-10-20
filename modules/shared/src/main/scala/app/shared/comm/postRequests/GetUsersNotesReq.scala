package app.shared.comm.postRequests

import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.entity.view.UsersNotes
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.generic.JsonCodec

object GetUsersNotesReq { //todo-now CONTINUE HERE
  import io.circe.generic.auto._
  import io.circe.syntax._
  import io.circe.generic.JsonCodec

  @JsonCodec
  case class Par(userID:EntityIdentity[User])
      extends PostRequest.Parameter

  @JsonCodec
  case class Res(
    optionEntity: Option[UsersNotes])
      extends PostRequest.Result
}

//@JsonCodec
class GetUsersNotesReq[V <: EntityType[V]]
    extends PostRequest[ReadRequest] {
  override type ParT     = GetUsersNotesReq.Par
  override type ResT     = GetUsersNotesReq.Res
  override type PayLoadT = V

}
