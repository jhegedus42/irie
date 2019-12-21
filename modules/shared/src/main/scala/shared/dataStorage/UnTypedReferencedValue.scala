package shared.dataStorage

import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shapeless.Typeable

@JsonCodec
case class UnTypedReferencedValue(
  unTypedRef: UnTypedRef,
  value:      UntypedVersionedValue)

object UnTypedReferencedValue {

  def fromTypedReferencedValue[V <: Value[V]](
    r: TypedReferencedValue[V]
  )(
    implicit
    enc:      Encoder[V],
    typeable: Typeable[V]
  ): UnTypedReferencedValue = {

    val unTypedRef: UnTypedRef = {
      val r2: TypedReferencedValue[V] = r.addTypeInfo()
      r2.ref.unTypedRef.addTypeInfo[V](typeable)
    }

    val untypedVersionedValue =
      UntypedVersionedValue(
        r.versionedEntityValue.version,
        UntypedValue.getFromValue(
          r.versionedEntityValue.valueWithoutVersion
        )
      )

    UnTypedReferencedValue(unTypedRef, untypedVersionedValue)

  }

}
