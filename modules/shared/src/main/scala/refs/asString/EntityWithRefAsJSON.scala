package refs.asString

import refs.EntityWithRef
import refs.entityValue.EntityType
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Json}

case class EntityWithRefAsJSON(json: Json) {

  def toEntityWithRef[V <: EntityType[V]]
  ( implicit d: Decoder[EntityWithRef[V]] ):
  Option[EntityWithRef[V]] = {
    val res: Result[EntityWithRef[V]] = d.decodeJson(json)
    res.toOption
  }
}

object EntityWithRefAsJSON {

  def fromEntityRef[V <: EntityType[V]](
    er: EntityWithRef[V]
  )(
    implicit e: Encoder[EntityWithRef[V]]
  ): EntityWithRefAsJSON = {
    EntityWithRefAsJSON(e.apply(er))
  }

}
