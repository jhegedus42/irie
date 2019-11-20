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

  def fromReferencedValue[V <: Value[V]](
    r: ReferencedValue[V]
  )(
    implicit
    enc:      Encoder[ReferencedValue[V]],
    typeable: Typeable[V]
  ): UnTypedReferencedValue = {
    val r2: ReferencedValue[V] = r.addTypeInfo()
    val j = r2.asJson
    UnTypedReferencedValue(r2.ref.unTypedRef.addTypeInfo[V](typeable), j)
  }

}
