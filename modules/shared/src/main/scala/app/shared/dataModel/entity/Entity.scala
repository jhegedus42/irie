package app.shared.dataModel.entity

import app.shared.dataModel.entity.Entity.{ Entity}

import scala.reflect.ClassTag

object Entity {

  trait Entity[T<:Entity[T]]
  {
//    def getTypedRef(implicit ct:ClassTag[T]) : TypedRef[T]=TypedRef.make[T]()
  }


}


case class EntityTypeAsString(type_as_string: String) {

//  def isTypeCorrect[E <: Data : ClassTag]: Boolean =
//    EntityTypeAsString.getTypeAsString[E] == type_as_string

}


object EntityTypeAsString {

  def make[T <: Entity[T]](implicit t: ClassTag[T]) =
    EntityTypeAsString(t.runtimeClass.getSimpleName)

  //  def make[T](t:Typeable[T])=EntityType(t.describe)


//  def getTypeAsString[T <: Data](implicit t: ClassTag[T]) =


//  def fromEntity(e: Data) =
//    EntityTypeAsString(e.getClass.getSimpleName)
}