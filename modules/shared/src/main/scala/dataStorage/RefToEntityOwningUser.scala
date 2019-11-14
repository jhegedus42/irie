package dataStorage

import io.circe.generic.JsonCodec

@JsonCodec
case class RefToEntityOwningUser(
  uuid: String = java.util.UUID.randomUUID().toString)

object RefToEntityOwningUser {

  def makeFromRef(r: Ref[User]) =
    RefToEntityOwningUser(r.unTypedRef.uuid)
}
