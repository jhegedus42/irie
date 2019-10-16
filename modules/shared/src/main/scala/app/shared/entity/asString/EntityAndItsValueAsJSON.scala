package app.shared.entity.asString

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import io.circe.Encoder
import monocle.macros.Lenses

@Lenses
case class EntityAndItsValueAsJSON(
  entityWithRefAsJSON: EntityWithRefAsJSON,
  entityValueAsJSON:   EntityValueAsJSON) {}

object EntityAndItsValueAsJSON {

  def make[V <: EntityType[V]](
    er: EntityWithRef[V]
  )(
    implicit e: Encoder[EntityWithRef[V]],
    ee:         Encoder[V]
  ): EntityAndItsValueAsJSON = {

    val erj = EntityWithRefAsJSON(e.apply(er))
    val ev  = EntityType.toJSON(er.entityValue)
    EntityAndItsValueAsJSON(erj, ev)
  }

}
