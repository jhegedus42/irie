package app.shared.dataModel.value.refs

import app.shared.dataModel.value.EntityValue
import io.circe._
import monocle.macros.Lenses

import scala.reflect.ClassTag

@Lenses
case class Entity[E <: EntityValue[E]](
    entityValue:       E,
    typedRefToEntity:  TypedRefToEntity[E],
    entityVersion:     EntityVersion = EntityVersion(),
    entityDeletedFlag: EntityDeletedFlag = EntityDeletedFlag( false )
) {

  def toJSON( implicit e: Encoder[Entity[E]] ): String =
    e.apply( this ).spaces4

}

object Entity {

  def makeFromEntity[E <: EntityValue[E]: ClassTag]( v: E ): Entity[E] =
    Entity( v, TypedRefToEntity.getDefaultValue[E] )

}
