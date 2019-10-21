package app.shared.entity

import app.shared.entity.asString.{
  EntityWithRefAsJSON,
  EntityAndItsValueAsJSON,
  EntityValueAsJSON,
  EntityValueTypeAsString
}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.refs.{
  EntityDeletedFlag,
  RefToEntityWithVersion
}
import io.circe._
import monocle.macros.Lenses

import scala.reflect.ClassTag

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

@JsonCodec
@Lenses
case class EntityWithRef[E <: EntityType[E]](
                                              entityValue: E,
                                              toRef: RefToEntityWithVersion[E]) {

  def updateValue(v: E): EntityWithRef[E] = this.copy(entityValue = v)

  def bumpVersion: EntityWithRef[E] =
    this.copy(toRef = this.toRef.bumpVersion)

}

object EntityWithRef {

  /**
    * @param v
    * @tparam V
    * @return Entity with random UUID and Version 0.
    */
  def makeFromValue[V <: EntityType[V]: ClassTag](
    v: V
  ): EntityWithRef[V] = {
    val tr: RefToEntityWithVersion[V] =
      RefToEntityWithVersion[V](
        EntityValueTypeAsString.getEntityValueTypeAsString[V]
      )
    EntityWithRef(v, tr)
  }

}
