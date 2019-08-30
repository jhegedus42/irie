package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.state
import app.shared.entity.Entity
import app.shared.entity.asString.EntityAsString
import app.shared.entity.entityValue.EntityValue
import io.circe.Encoder

private[persistentActor] case class StateMapSnapshot(
    val map: Map[UntypedRef, StateMapEntry] = Map.empty
) {
  def insertEntity[V <: EntityValue[V]](
      entity: Entity[V]
  )(
      implicit
      encoder: Encoder[Entity[V]]
  ): StateMapSnapshot = {

    val utr =
      UntypedRef.makeFromRefToEntity(entity.refToEntity)
    val entityAsString: EntityAsString =
      entity.entityAsString()
    val entry =
      StateMapEntry(utr, entityAsString)

    val newMap : Map[UntypedRef, StateMapEntry] = this.map + (utr -> entry)
    StateMapSnapshot(newMap)
  }
}
