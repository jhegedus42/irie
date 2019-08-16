package app.shared.dataModel.value.asString

import app.shared.dataModel.value.EntityValue

import scala.reflect.ClassTag

case class EntityValueTypeAsString(type_as_string: String)


object EntityValueTypeAsString {

  def make[T <: EntityValue[T]](implicit t: ClassTag[T]) =
    EntityValueTypeAsString(t.runtimeClass.getSimpleName)

}