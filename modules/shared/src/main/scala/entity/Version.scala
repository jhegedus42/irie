package entity

import dataModel.EntityValueType
import io.circe.generic.JsonCodec
import shapeless.T

//@Lenses
@JsonCodec
case class Version[V<:EntityValueType[T]](versionNumberLong: Long = 0 ) {

  def bumpVersion(): Version[V] =
    this.copy( versionNumberLong = this.versionNumberLong + 1 )
}
