package dataModel

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import entity.{Ref, EntityType}

@JsonCodec
case class Image(
                 title:String,
                 content: String,
                 reference:Option[Ref[Note]])
  extends Entity[Image]

