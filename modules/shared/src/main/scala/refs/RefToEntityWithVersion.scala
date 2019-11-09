package refs

import refs.asString.EntityValueTypeAsString
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

import scala.reflect.ClassTag

//@Lenses
//@JsonCodec
case class RefToEntityWithVersion[T <: EntityType[T]](
  entityValueTypeAsString: EntityValueTypeAsString,
  entityIdentity:          EntityIdentity[T] = EntityIdentity[T](),
  entityVersion:           EntityVersion = EntityVersion()) {

//  def toEntityRef[T<:EntityType[T]]: Unit ={
//    new EntityWithRef[T]()
//  }

  def bumpVersion: RefToEntityWithVersion[T] =
    this.copy(entityVersion = entityVersion.bumpVersion())
}

object RefToEntityWithVersion {

  def fromEntityIdentity[T <: EntityType[T]: ClassTag](
    identity: EntityIdentity[T]
  ): RefToEntityWithVersion[T] = {
    RefToEntityWithVersion(
      EntityValueTypeAsString.getEntityValueTypeAsString[T],
      identity
    )
  }

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
