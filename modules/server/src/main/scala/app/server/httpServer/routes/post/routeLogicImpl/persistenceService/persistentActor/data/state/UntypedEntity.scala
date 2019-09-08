package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state

import app.shared.entity.Entity
import app.shared.entity.asString.{EntityAndItsValueAsJSON, EntityValueAsJSON}
import app.shared.entity.entityValue.EntityValue
import io.circe.Encoder
import monocle.macros.Lenses

@Lenses
private[persistentActor] case class UntypedEntity(
    untypedRef:              UntypedRef,
//    entityAndItsValueAsJSON: EntityAndItsValueAsJSON
//      entityAndItsValueAsJSON: EntityAndItsValueAsJSON
        entityValueAsJSON: EntityValueAsJSON
)

object UntypedEntity {

  implicit def makeFromEntity[V <: EntityValue[V]](e: Entity[V])(
      implicit encoder: Encoder[Entity[V]],
      ee:Encoder[V]
  ): UntypedEntity = {
    val utr: UntypedRef = e.refToEntity
//    val entityAsString = e.entityAsJSON()
    val entityAsString: EntityValueAsJSON = EntityValue.getAsJson(e.entityValue)
    UntypedEntity(utr, entityAsString)
  }

  def getTypedEntity[V<:EntityValue[V]]:Entity[V]={

    ???
  }

}
