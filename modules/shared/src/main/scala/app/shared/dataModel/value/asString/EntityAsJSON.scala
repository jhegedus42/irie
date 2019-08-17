package app.shared.dataModel.value.asString

import app.shared.dataModel.value.EntityValue

case class EntityAsJSON(json:String)

object EntityAsJSON {
  def getEntity[V<:EntityValue[V]] = ???
}
