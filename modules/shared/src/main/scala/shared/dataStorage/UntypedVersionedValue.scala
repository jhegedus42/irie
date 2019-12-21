package shared.dataStorage

import io.circe.Json

case class UntypedVersionedValue(
  version: EntityVersion,
  value:   UntypedValue)
