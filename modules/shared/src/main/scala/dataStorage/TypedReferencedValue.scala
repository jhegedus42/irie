package dataStorage

import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec
import cats._
//import cats.derived
import cats.implicits._
import shapeless.Typeable

@JsonCodec
case class TypedReferencedValue[E <: Value[E]](
  entityValue: E,
  ref:         Ref[E] = Ref[E]()) {

  def addTypeInfo(
  )(
    implicit
    typeable: Typeable[E]
  ): TypedReferencedValue[E] = {
    val r: Ref[E] = ref.addTypeInfo()(typeable)
    TypedReferencedValue(entityValue, r)
  }

  def addEntityOwnerInfo(r: Ref[User]): TypedReferencedValue[E] = {
    TypedReferencedValue(this.entityValue, this.ref.addEntityOwnerInfo(r))
  }

}

object TypedReferencedValue {}
