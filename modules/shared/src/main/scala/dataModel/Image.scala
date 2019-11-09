package dataModel

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import refs.{EntityIdentity, EntityType}

@JsonCodec
case class Image(
                 title:String,
                 content: String,
                 reference:Option[EntityIdentity[Note]])
  extends EntityType[Image]

