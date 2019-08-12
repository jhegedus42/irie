package app.shared.dataModel.entity.refs

import app.shared.dataModel.entity.Entity
import app.shared.dataModel.entity.EntityTypeAsString
import app.shared.utils.UUID_Utils.{EntityUUID}
import monocle.macros.Lenses

import scala.reflect.ClassTag

@Lenses
case class TypedRef[T <: Entity[T]]( uuid:     EntityUUID, dataType: EntityTypeAsString )

object TypedRef {
  def getDefaultValue[T <: Entity[T]](implicit t:ClassTag[T]): TypedRef[T] ={
    val uuid:     EntityUUID         = EntityUUID()
    val dataType: EntityTypeAsString = EntityTypeAsString.make[T]
    TypedRef[T](uuid,dataType)
  }
}

