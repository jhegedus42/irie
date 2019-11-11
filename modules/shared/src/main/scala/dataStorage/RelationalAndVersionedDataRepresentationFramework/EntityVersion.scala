package dataStorage.RelationalAndVersionedDataRepresentationFramework

import dataStorage.normalizedDataModel.EntityValueType

import dataStorage.normalizedDataModel.EntityValueType
import io.circe._


import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

//@Lenses
@JsonCodec
case class EntityVersion[V<:EntityValueType[V]](versionNumberLong: Long = 0 ) {

  def bumpVersion(): EntityVersion[V] =
    this.copy( versionNumberLong = this.versionNumberLong + 1 )
}
