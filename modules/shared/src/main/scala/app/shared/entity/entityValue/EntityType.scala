package app.shared.entity.entityValue

import app.shared.entity.asString.EntityValueAsJSON
import io.circe.generic.JsonCodec
import io.circe.{Encoder, Json}

/**
  *
  * This is the supertype of the possible types of the value
  * which an entity can contain. Every entity contains only
  * one value.
  *
  * @tparam T
  */
trait EntityType[+T <: EntityType[T]] {}

object EntityType {

  implicit def toJSON[T <: EntityType[T]](
    v: T
  )(
    implicit encoder: Encoder[T]
  ): EntityValueAsJSON =
    EntityValueAsJSON(encoder.apply(v))


}
