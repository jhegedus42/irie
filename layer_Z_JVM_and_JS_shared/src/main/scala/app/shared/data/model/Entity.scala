package app.shared.data.model

import app.shared.data.model.Entity.Data

import scala.reflect.ClassTag

object Entity {
//  sealed trait Data

  // Random UUID: 1bac6f8972b243bf9a9e1b636b871d4a
  // commit 261bba625a6dc3bfc178a1d578cd104b23cf6437
  // Date: Tue Aug  7 01:28:12 EEST 2018


  trait Value

//  trait EntityParams

  trait Entity extends Data

  trait Data
  {
    def constraints:List[Constraint]= List()

//    def getType[T2:Typeable]() =EntityType.make[T2](implicitly[Typeable[T2]])
  }
  abstract class Constraint(){
//    def isSatisfied(state:State) : Boolean
  }

//  override def toString: String = this.asJS
}


case class TypeAsString(type_as_string:String){

  def isTypeCorrect[E<:Data:ClassTag]:Boolean= TypeAsString.getTypeAsString[E] == type_as_string
}


object TypeAsString{
//  def make[T](t:Typeable[T])=EntityType(t.describe)
    def getTypeAsString[T<:Data](implicit t:ClassTag[T])= t.runtimeClass.getSimpleName
    def make[T<:Data](implicit t:ClassTag[T])= TypeAsString(getTypeAsString[T])
    def fromEntity(e:Data)= TypeAsString(e.getClass.getSimpleName)
}