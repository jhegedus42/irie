package app.shared.entity.asString

import app.shared.entity.entityValue.EntityValue
import monocle.macros.Lenses

import scala.reflect.ClassTag

@Lenses
case class EntityValueTypeAsString(type_as_string: String)

object EntityValueTypeAsString {

  def make[T <: EntityValue[T]](implicit t: ClassTag[T]) =
    EntityValueTypeAsString(t.runtimeClass.getSimpleName)

}
