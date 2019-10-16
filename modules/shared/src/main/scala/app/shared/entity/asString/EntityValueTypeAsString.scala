package app.shared.entity.asString

import app.shared.entity.entityValue.EntityType
import monocle.macros.Lenses

import scala.reflect.ClassTag

@Lenses
case class EntityValueTypeAsString(type_as_string: String)

object EntityValueTypeAsString {

  def getEntityValueTypeAsString[T <: EntityType[T]](
    implicit t: ClassTag[T]
  ): EntityValueTypeAsString =
    EntityValueTypeAsString(t.runtimeClass.getSimpleName)

}
