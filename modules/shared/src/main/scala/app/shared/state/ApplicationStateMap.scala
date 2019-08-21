package app.shared.state

import app.shared.entity.Entity
import app.shared.entity.asString.EntityAsString
import app.shared.entity.entityValue.EntityValue
import io.circe.Encoder

case class ApplicationStateMap (
  val map: Map[UntypedRef, ApplicationStateMapEntry] = Map.empty
) {

  def insertEntity[V <: EntityValue[V]](entity: Entity[V])(
    implicit encoder: Encoder[Entity[V]]
  ): ApplicationStateMap = {

    val utr = UntypedRef.makeFromRefToEntity(entity.refToEntity)
    val entityAsString: EntityAsString = entity.entityAsString()
    val entry = ApplicationStateMapEntry(utr, entityAsString)
    val newMap: Map[UntypedRef, ApplicationStateMapEntry] = map + (utr -> entry)
    ApplicationStateMap(newMap)
  }

}