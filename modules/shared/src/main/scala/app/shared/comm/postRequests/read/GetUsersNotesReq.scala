package app.shared.comm.postRequests.read

import app.shared.comm.{PostRequest, ReadRequest}
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.generic.JsonCodec

object GetUsersNotesReq {
  import io.circe.generic.JsonCodec
  import io.circe.generic.auto._
  import io.circe.syntax._

  @JsonCodec
  case class Par(userID: EntityIdentity[User])
      extends PostRequest.Parameter

  @JsonCodec
  case class Res(maybeSet: Option[Set[RefToEntityWithVersion[Note]]])
      extends PostRequest.Result
}

//@JsonCodec
class GetUsersNotesReq extends PostRequest[ReadRequest] with ReadRequest {
  override type ParT     = GetUsersNotesReq.Par
  override type ResT     = GetUsersNotesReq.Res
  override type PayLoadT = Unit

}
