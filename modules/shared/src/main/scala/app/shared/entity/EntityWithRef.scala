package app.shared.entity

import app.shared.entity.asString.{
  EntityAsJSON,
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
  entityValue:       E,
  refToEntity:       RefToEntityWithVersion[E]){

  def entityAsJSON(
  )(
    implicit e: Encoder[EntityWithRef[E]],
    ee:         Encoder[E]
  ): EntityAndItsValueAsJSON = {
    EntityAndItsValueAsJSON(EntityAsJSON(e.apply(this)),
                            EntityType.getAsJson(entityValue))
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
  def makeFromValue[V <: EntityType[V]: ClassTag](
    v: V
  ): EntityWithRef[V] = {
    val tr =
      RefToEntityWithVersion[V](EntityValueTypeAsString.getEntityValueTypeAsString[V])
    EntityWithRef(v, tr)
  }


}
