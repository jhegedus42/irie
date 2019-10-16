package app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state

import app.shared.entity.EntityWithRef
import app.shared.entity.asString.{
  EntityAndItsValueAsJSON,
  EntityValueAsJSON
}
import app.shared.entity.entityValue.EntityType
import io.circe.{Decoder, Encoder}
import monocle.macros.Lenses

@Lenses
private[persistentActor] case class UntypedEntityWithRef(
  untypedRef:              UntypedRef,
  entityAndItsValueAsJSON: EntityAndItsValueAsJSON) {}

//  entityAndItsValueAsJSON: EntityAndItsValueAsJSON
//  entityValueAsJSON: EntityValueAsJSON)

object UntypedEntityWithRef {

  implicit def makeFromEntity[V <: EntityType[V]](
    e: EntityWithRef[V]
  )(
    implicit encoder: Encoder[EntityWithRef[V]],
    ee:               Encoder[V]
  ): UntypedEntityWithRef = {
    val utr: UntypedRef = e.refToEntity
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
