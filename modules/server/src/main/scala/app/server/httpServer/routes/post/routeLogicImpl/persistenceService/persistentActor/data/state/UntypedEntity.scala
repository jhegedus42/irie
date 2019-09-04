package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state

import app.shared.entity.Entity
import app.shared.entity.asString.EntityAsString
import app.shared.entity.entityValue.EntityValue
import io.circe.Encoder
import monocle.macros.Lenses

@Lenses
private[persistentActor] case class UntypedEntity(
    untypedRef:     UntypedRef,
    entityAsString: EntityAsString
) {

  def asSimpleString(): String = {
    val s1 = untypedRef.asSimpleString()
    val s2 =
      entityAsString.entityValueAsToString.toStringResult
    s"$s1 ----- $s2"
  }
}

object UntypedEntity {

  def makeFromEntity[V <: EntityValue[V]](e: Entity[V])(
      implicit encoder: Encoder[Entity[V]]
  ): UntypedEntity = {
    val utr: UntypedRef = e.refToEntity
    val entityAsString = e.entityAsString()
    UntypedEntity(utr, entityAsString)
  }
}
