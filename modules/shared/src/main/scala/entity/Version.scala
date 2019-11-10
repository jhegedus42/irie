package entity

import dataModel.EntityValueType

import dataModel.EntityValueType
import io.circe._


import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

//@Lenses
@JsonCodec
case class Version[V<:EntityValueType[V]](versionNumberLong: Long = 0 ) {

  def bumpVersion(): Version[V] =
    this.copy( versionNumberLong = this.versionNumberLong + 1 )
}
