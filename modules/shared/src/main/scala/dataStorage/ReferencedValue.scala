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

  def addTypeInfo(
  )(
    implicit
    typeable: Typeable[E]
  ): ReferencedValue[E] = {
    val r: Ref[E] = ref.addTypeInfo()(typeable)
    ReferencedValue(entityValue, r)
  }

  def addEntityOwnerInfo(r: Ref[User]): ReferencedValue[E] = {
    ReferencedValue(this.entityValue, this.ref.addEntityOwnerInfo(r))
  }

}

object ReferencedValue {}
