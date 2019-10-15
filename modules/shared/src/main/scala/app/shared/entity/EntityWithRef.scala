package app.shared.entity

import app.shared.entity.asString.{
  EntityAsJSON,
  EntityAndItsValueAsJSON,
  EntityValueAsJSON,
  EntityValueTypeAsString
}
import app.shared.entity.entityValue.EntityValue
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
case class EntityWithRef[E <: EntityValue[E]](
  entityValue:       E,
  refToEntity:       RefToEntityWithVersion[E]){

  def entityAsJSON(
  )(
    implicit e: Encoder[EntityWithRef[E]],
    ee:         Encoder[E]
  ): EntityAndItsValueAsJSON = {
    EntityAndItsValueAsJSON(EntityAsJSON(e.apply(this)),
                            EntityValue.getAsJson(entityValue))
  }

  def updateValue(v:E): EntityWithRef[E] =this.copy(entityValue=v)
  def bumpVersion: EntityWithRef[E] =this.copy(refToEntity=this.refToEntity.bumpVersion)



}


object EntityWithRef {

  /**
    * @param v
    * @tparam V
    * @return Entity with random UUID and Version 0.
    */
  def makeFromValue[V <: EntityValue[V]: ClassTag](
    v: V
  ): EntityWithRef[V] = {
    val tr =
      RefToEntityWithVersion[V](EntityValueTypeAsString.make[V])
    EntityWithRef(v, tr)
  }


}
