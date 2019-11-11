package dataStorage.RelationalAndVersionedDataRepresentationFramework

import dataStorage.normalizedDataModel.EntityValueType
import io.circe.generic.JsonCodec

@JsonCodec
case class EntityIdentity[V <: EntityValueType[V]](
  uuid: String = java.util.UUID.randomUUID().toString)
