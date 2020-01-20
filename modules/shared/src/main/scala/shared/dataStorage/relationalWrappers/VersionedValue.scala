package shared.dataStorage.relationalWrappers

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shared.dataStorage.model.Value

@JsonCodec
case class VersionedValue[V <: Value[V]](
  valueWithoutVersion: V,
  version:             EntityVersion = EntityVersion())

object VersionedValue {
  implicit def conv[V <: Value[V]](v: V) = VersionedValue(v)
}
