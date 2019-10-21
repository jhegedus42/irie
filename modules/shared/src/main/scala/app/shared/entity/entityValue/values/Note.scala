package app.shared.entity.entityValue.values

import app.shared.entity.entityValue.EntityType
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe._

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

@JsonCodec
case class Note(
               title:String,
  content: String,
  owner:   RefToEntityWithVersion[User])
    extends EntityType[Note]
