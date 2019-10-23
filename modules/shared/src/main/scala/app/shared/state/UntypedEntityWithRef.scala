package app.shared.state

import app.shared.entity.EntityWithRef
import app.shared.entity.asString.EntityAndItsValueAsJSON
import app.shared.entity.entityValue.EntityType
import io.circe.{Decoder, Encoder}
import monocle.macros.Lenses

@Lenses
case class UntypedEntityWithRef(
  untypedRef:              UntypedRef,
  entityAndItsValueAsJSON: EntityAndItsValueAsJSON) {}

//  entityAndItsValueAsJSON: EntityAndItsValueAsJSON
//  entityValueAsJSON: EntityValueAsJSON)

object UntypedEntityWithRef {

  implicit def makeFromEntityWithRef[V <: EntityType[V]](
    e: EntityWithRef[V]
  )(
    implicit encoder: Encoder[EntityWithRef[V]],
    ee:               Encoder[V]
  ): UntypedEntityWithRef = {
    val utr: UntypedRef = e.toRef
    val entityAsString: EntityAndItsValueAsJSON =
      EntityAndItsValueAsJSON.make(e)
    UntypedEntityWithRef(utr, entityAsString)
  }

  def toTypedEntityWithRef[V <: EntityType[V]](
    ute: UntypedEntityWithRef
  )(
    implicit d: Decoder[EntityWithRef[V]]
  ): Option[EntityWithRef[V]] = {
    ute.entityAndItsValueAsJSON.entityWithRefAsJSON.toEntityWithRef[V]
  }

}