package refs.asString

import refs.{EntityType, EntityWithRef}
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
