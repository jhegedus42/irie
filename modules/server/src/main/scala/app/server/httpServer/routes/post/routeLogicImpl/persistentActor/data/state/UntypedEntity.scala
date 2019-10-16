package app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state

import app.shared.entity.EntityWithRef
import app.shared.entity.asString.{
  EntityAndItsValueAsJSON,
  EntityValueAsJSON
}
import app.shared.entity.entityValue.EntityType
import io.circe.Encoder
import monocle.macros.Lenses

@Lenses
private[persistentActor] case class UntypedEntity(
  untypedRef:              UntypedRef,
  entityAndItsValueAsJSON: EntityAndItsValueAsJSON) {}

//      entityAndItsValueAsJSON: EntityAndItsValueAsJSON
//  entityValueAsJSON: EntityValueAsJSON)

object UntypedEntity {

  implicit def makeFromEntity[V <: EntityType[V]]
  ( e: EntityWithRef[V] )
  ( implicit encoder: Encoder[EntityWithRef[V]],
     ee:               Encoder[V]
  ): UntypedEntity = {
    val utr:            UntypedRef              = e.refToEntity
    val entityAsString: EntityAndItsValueAsJSON = e.entityAsJSON()
    UntypedEntity(utr, entityAsString)
  }

  def converToTypedEntityWithRef[V <: EntityType[V]](
    ute: UntypedEntity
  ): EntityWithRef[V] = {

    ??? //todo-now 1.1.1.1
  }

}
