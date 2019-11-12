package dataStorage

import io.circe.generic.JsonCodec

@JsonCodec
case class UserRef(
  uuid: String = java.util.UUID.randomUUID().toString)
