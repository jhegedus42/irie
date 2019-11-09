package refs

trait EntityType[+T <: EntityType[T]] {}

import io.circe.Encoder
import io.circe.generic.JsonCodec
import refs.asString.EntityValueAsJSON

object EntityType {

  implicit def toJSON[T <: EntityType[T]](
    v: T
  )(
    implicit encoder: Encoder[T]
  ): EntityValueAsJSON =
    EntityValueAsJSON(encoder.apply(v))

}
