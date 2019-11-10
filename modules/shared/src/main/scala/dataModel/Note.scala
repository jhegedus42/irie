package dataModel

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import refs.{Identity, EntityType}

@JsonCodec
case class Note(
  title:   String,
  content: String,
  owner:   Identity[User])
    extends EntityType[Note]
