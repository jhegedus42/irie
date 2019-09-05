package app.shared.entity.entityValue

import app.shared.entity.asString.EntityValueAsJSON
import io.circe.{Encoder, Json}

/**
  *
  * This is the supertype of the possible types of the value
  * which an entity can contain. Every entity contains only
  * one value.
  *
  * @tparam T
  */
trait EntityValue[T <: EntityValue[T]] {}

object EntityValue {

  def getAsJson[T <: EntityValue[T]](
      v:              T
  )(implicit encoder: Encoder[T]): EntityValueAsJSON =
    EntityValueAsJSON(encoder.apply(v))

}
