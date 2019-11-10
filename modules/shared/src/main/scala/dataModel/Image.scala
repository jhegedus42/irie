package dataModel

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import refs.{Identity, EntityType}

@JsonCodec
case class Image(
                 title:String,
                 content: String,
                 reference:Option[Identity[Note]])
  extends EntityType[Image]

