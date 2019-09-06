package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state

import app.shared.entity.Entity
import app.shared.entity.asString.EntityAndItsValueAsJSON
import app.shared.entity.entityValue.EntityValue
import io.circe.Encoder
import monocle.macros.Lenses

@Lenses
private[persistentActor] case class UntypedEntity(
    untypedRef:              UntypedRef,
    entityAndItsValueAsJSON: EntityAndItsValueAsJSON
)

object UntypedEntity {

  implicit def makeFromEntity[V <: EntityValue[V]](e: Entity[V])(
      implicit encoder: Encoder[Entity[V]],
      ee:Encoder[V]
  ): UntypedEntity = {
    val utr: UntypedRef = e.refToEntity
    val entityAsString = e.entityAsJSON()
    UntypedEntity(utr, entityAsString)
  }
}
