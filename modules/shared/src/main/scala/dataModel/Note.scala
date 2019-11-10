package dataModel

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import entity.{Ref, EntityType}

@JsonCodec
case class Note(
  title:   String,
  content: String,
  owner:   Ref[User])
    extends Entity[Note]
