package app.shared.dataModel.entity


import scala.reflect.ClassTag

case class EntityTypeAsString(type_as_string: String)


object EntityTypeAsString {

  def make[T <: Entity[T]](implicit t: ClassTag[T]) =
    EntityTypeAsString(t.runtimeClass.getSimpleName)

}