package shared.dataStorage.relationalWrappers

import io.circe.generic.JsonCodec
import shared.dataStorage.model.User

@JsonCodec
case class RefToEntityOwningUser(
  uuid: String = java.util.UUID.randomUUID().toString)

object RefToEntityOwningUser {

  def makeFromRef(r: Ref[User]) =
    RefToEntityOwningUser(r.unTypedRef.uuid)
}
