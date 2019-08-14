package app.shared.dataModel.value


import scala.reflect.ClassTag

case class ValueTypeAsString(type_as_string: String)


object ValueTypeAsString {

  def make[T <: EntityValue[T]](implicit t: ClassTag[T]) =
    ValueTypeAsString(t.runtimeClass.getSimpleName)

}