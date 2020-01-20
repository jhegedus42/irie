package shared.dataStorage.relationalWrappers

import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shapeless.Typeable
import shared.dataStorage.model.Value

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

  def toTypedReferencedValue[V <: Value[V]](
    unTypedReferencedValue: UnTypedReferencedValue
  )(
    implicit
    d: Decoder[V]
  ): Option[TypedReferencedValue[V]] = {

    val veo: Option[VersionedValue[V]] = UntypedVersionedValue
      .getVersionedValue[V](unTypedReferencedValue.value)

    val r =
      UnTypedRef.toTypedRef[V](unTypedReferencedValue.unTypedRef)
    val res: Option[TypedReferencedValue[V]] = for {
      ve <- veo
    } yield (TypedReferencedValue[V](ve, r))
    res
  }

}
