package app.shared.entity.refs

import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.entityValue.EntityValue
import app.shared.utils.UUID_Utils.EntityIdentity
import monocle.macros.Lenses

@Lenses
case class RefToEntityWithVersion[T <: EntityValue[T]](
  entityValueTypeAsString: EntityValueTypeAsString,
  entityIdentity:          EntityIdentity = EntityIdentity(),
  entityVersion:           EntityVersion = EntityVersion()) {

  def bumpVersion: RefToEntityWithVersion[T] =
    this.copy(entityVersion = entityVersion.bumpVersion())
}

object RefToEntityWithVersion {

  def getOnlyLatestVersions[V <: EntityValue[V]](
    l: List[RefToEntityWithVersion[V]]
  ) = {
    val grouped: Map[String, List[RefToEntityWithVersion[V]]] =
      l.groupBy(_.entityIdentity.uuid)
    val g2 =
      grouped.toList.map(
        t => (t._1, t._2.maxBy(_.entityVersion.versionNumberLong))
      )
    val map_ : Map[String, RefToEntityWithVersion[V]] = g2.toMap

    val res = map_.values.toList
    res
  }

}
