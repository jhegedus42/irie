package app.shared.entity.asString

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import io.circe.Decoder.Result
import io.circe.{Decoder, Json}

case class EntityAsJSON(json: Json) {

  def getEntity[V <: EntityType[V]](
    implicit d: Decoder[EntityWithRef[V]]
  ): Option[EntityWithRef[V]] = {
    val res: Result[EntityWithRef[V]] = d.decodeJson(json)
    res.toOption
  }

}
