package app.shared.dataModel.value.asString

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.Entity

case class EntityAsJSON( json: String )

object EntityAsJSON {
  def getEntity[V <: EntityValue[V]]( entityAsJSON: EntityAsJSON ) :
    Option[Entity[V]] = ???

}
