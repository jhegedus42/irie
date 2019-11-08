package app.shared.entity.entityValue.values

import app.shared.entity.entityValue.EntityType
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

@JsonCodec
case class Image(
                 title:String,
                 content: String,
                 reference:Option[EntityIdentity[Note]])
  extends EntityType[Image]

