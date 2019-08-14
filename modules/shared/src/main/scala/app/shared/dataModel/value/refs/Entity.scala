package app.shared.dataModel.value.refs

import app.shared.dataModel.value.{EntityValue, EntityValueTypeAsString}
import io.circe._
import monocle.macros.Lenses

import scala.reflect.ClassTag

@Lenses
case class Entity[E <: EntityValue[E]](
    entityValue:       E,
    refToEntity:       RefToEntity[E],
    entityDeletedFlag: EntityDeletedFlag = EntityDeletedFlag( false )
) {

  def toJSON( implicit e: Encoder[Entity[E]] ): String =
    e.apply( this ).spaces4

}

object Entity {

  def makeFromValue[V <: EntityValue[V]: ClassTag]( v: V ): Entity[V] = {
    val tr = RefToEntity[V]( EntityValueTypeAsString.make[V] )
    Entity( v, tr )
  }

}
