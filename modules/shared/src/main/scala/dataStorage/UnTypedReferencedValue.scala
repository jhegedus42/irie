package dataStorage

import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shapeless.Typeable

@JsonCodec
case class UnTypedReferencedValue(
  t:    UnTypedRef,
  json: Json)

object UnTypedReferencedValue {

  def fromTypedReferencedValue[V <: Value[V]](
    r: TypedReferencedValue[V]
  )(
    implicit
    enc:      Encoder[TypedReferencedValue[V]],
    typeable: Typeable[V]
  ): UnTypedReferencedValue = {
    val r2: TypedReferencedValue[V] = r.addTypeInfo()
    val j = r2.asJson
    UnTypedReferencedValue(r2.ref.unTypedRef.addTypeInfo[V](typeable), j)
  }

}
