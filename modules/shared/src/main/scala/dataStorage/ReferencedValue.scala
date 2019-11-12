package dataStorage

import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec
import cats._
//import cats.derived
import cats.implicits._
import shapeless.Typeable

@JsonCodec
case class ReferencedValue[E <: Value[E]](
  entityValue: E,
  ref:         Ref[E] = Ref[E]()) {

//  def addClassName()(
//    implicit t: Typeable[E]
//  ): ReferencedValue[E] =
//    this.copy[E](ref = ref.addClassName)

}
