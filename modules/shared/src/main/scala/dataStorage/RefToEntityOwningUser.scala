package dataStorage

import io.circe.generic.JsonCodec

@JsonCodec
case class RefToEntityOwningUser(
  uuid: String = java.util.UUID.randomUUID().toString)
