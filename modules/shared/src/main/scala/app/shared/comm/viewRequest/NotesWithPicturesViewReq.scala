package app.shared.comm.viewRequest

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.{Image, Note, User}
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.generic.JsonCodec


object NotesWithPicturesViewReq {

  import io.circe.generic.JsonCodec
  import io.circe.generic.auto._
  import io.circe.syntax._

  @JsonCodec
  case class NotesWithPictures(note:EntityWithRef[Note], image:EntityWithRef[Image])

  @JsonCodec
  case class Par(userID: EntityIdentity[User])
    extends ViewRequest.Parameter

  @JsonCodec
  case class Res(maybeSet: Option[Set[NotesWithPictures]])
    extends ViewRequest.Result
}

//@JsonCodec
class NotesWithPicturesViewReq extends ViewRequest{
  override type ParT     = NotesWithPicturesViewReq.Par
  override type ResT     = NotesWithPicturesViewReq.Res
//  override type PayLoadT = Unit

}
