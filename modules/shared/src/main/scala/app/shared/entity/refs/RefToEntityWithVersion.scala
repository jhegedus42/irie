package app.shared.entity.refs

import app.shared.entity.EntityWithRef
import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.entityValue.EntityType
import app.shared.utils.UUID_Utils.EntityIdentity
import monocle.macros.Lenses

@Lenses
case class RefToEntityWithVersion[T <: EntityType[T]](
  entityValueTypeAsString: EntityValueTypeAsString,
  entityIdentity:          EntityIdentity[T] = EntityIdentity[T](),
  entityVersion:           EntityVersion = EntityVersion()) {

  def toEntityRef[T<:EntityType[T]]: Unit ={
    new EntityWithRef[T]()
  }

  def bumpVersion: RefToEntityWithVersion[T] =
    this.copy(entityVersion = entityVersion.bumpVersion())
}

object RefToEntityWithVersion {

  def getOnlyLatestVersions[V <: EntityType[V]](
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
