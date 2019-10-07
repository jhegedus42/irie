package app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state

import app.shared.entity.EntityWithRef
import app.shared.entity.asString.{
  EntityAndItsValueAsJSON,
  EntityValueAsJSON
}
import app.shared.entity.entityValue.EntityValue
import io.circe.Encoder
import monocle.macros.Lenses

@Lenses
private[persistentActor] case class UntypedEntity(
  untypedRef:              UntypedRef,
  entityAndItsValueAsJSON: EntityAndItsValueAsJSON,
//      entityAndItsValueAsJSON: EntityAndItsValueAsJSON
  entityValueAsJSON: EntityValueAsJSON)

object UntypedEntity {

  implicit def makeFromEntity[V <: EntityValue[V]](
    e: EntityWithRef[V]
  )(
    implicit encoder: Encoder[EntityWithRef[V]],
    ee:               Encoder[V]
  ): UntypedEntity = {
    val utr:            UntypedRef              = e.refToEntity
    val entityAsString: EntityAndItsValueAsJSON = e.entityAsJSON()
    val entityValueAsString: EntityValueAsJSON =
      EntityValue.getAsJson(e.entityValue)
//    UntypedEntity(utr, entityValueAsString)
    UntypedEntity(utr, entityAsString, entityValueAsString)
  }

  def converToTypedEntityWithRef[V <: EntityValue[V]](
    ute: UntypedEntity
  ): EntityWithRef[V] = {

    // todo-now continue-now
    ???
  }

}
