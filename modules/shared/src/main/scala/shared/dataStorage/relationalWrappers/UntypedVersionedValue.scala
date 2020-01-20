package shared.dataStorage.relationalWrappers

import io.circe.Decoder
import io.circe.Decoder.Result
import shared.dataStorage.model.Value

case class UntypedVersionedValue(
  version: EntityVersion,
  value:   UntypedValue)

object UntypedVersionedValue {

  def getVersionedValue[V <: Value[V]](
    untypedVersionedValue: UntypedVersionedValue
  )(
    implicit
    d: Decoder[V]
  ): Option[VersionedValue[V]] = {
    val json = untypedVersionedValue.value.json
    val res:  Result[V] = d.decodeJson(json)
    val reso: Option[V] = res.toOption
    for {
      v <- reso
    } yield (VersionedValue[V](v, untypedVersionedValue.version))
  }

}
