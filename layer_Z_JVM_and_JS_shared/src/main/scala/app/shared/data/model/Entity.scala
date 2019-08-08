package app.shared.data.model

import app.shared.data.model.Entity.Data
import app.shared.data.ref.TypedRef

import scala.reflect.ClassTag

object Entity {

  trait Entity[T<:Entity[T]] extends Data
  {
    def getTypedRef(implicit ct:ClassTag[T]) : TypedRef[T]=TypedRef.make[T]()
  }

  trait Data

}


case class TypeAsString(type_as_string: String) {

  def isTypeCorrect[E <: Data : ClassTag]: Boolean =
    TypeAsString.getTypeAsString[E] == type_as_string
}


object TypeAsString {

  def make[T <: Data](implicit t: ClassTag[T]) =
    TypeAsString(getTypeAsString[T])

  //  def make[T](t:Typeable[T])=EntityType(t.describe)


  def getTypeAsString[T <: Data](implicit t: ClassTag[T]) =
    t.runtimeClass.getSimpleName

  def fromEntity(e: Data) =
    TypeAsString(e.getClass.getSimpleName)
}