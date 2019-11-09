package dataModel

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import refs.{EntityIdentity, EntityType}

@JsonCodec
case class Note(
  title:   String,
  content: String,
  owner:   EntityIdentity[User])
    extends EntityType[Note]
