package app.shared.entity

import app.shared.entity.asString.{
  EntityAsJSON,
  EntityAndItsValueAsJSON,
  EntityValueAsJSON,
  EntityValueTypeAsString
}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.{EntityDeletedFlag, RefToEntityWithVersion}
import io.circe._
import monocle.macros.Lenses

import scala.reflect.ClassTag

@Lenses
case class Entity[E <: EntityValue[E]](
    entityValue:       E,
    refToEntity:       RefToEntityWithVersion[E],
    entityDeletedFlag: EntityDeletedFlag = EntityDeletedFlag(false)
) {

  def entityAsJSON()(
      implicit e: Encoder[Entity[E]],
      ee:         Encoder[E]
  ): EntityAndItsValueAsJSON = {
    EntityAndItsValueAsJSON(EntityAsJSON(e.apply(this)),
                            EntityValue.getAsJson(entityValue))
  }



}

object Entity {

  /**
    * @param v
    * @tparam V
    * @return Entity with random UUID and Version 0.
    */
  def makeFromValue[V <: EntityValue[V]: ClassTag](v: V): Entity[V] = {
    val tr = RefToEntityWithVersion[V](EntityValueTypeAsString.make[V])
    Entity(v, tr)
  }

}
