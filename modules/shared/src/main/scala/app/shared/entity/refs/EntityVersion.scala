package app.shared.entity.refs

//import io.circe.generic.JsonCodec
import monocle.macros.Lenses
//import io.circe.generic.auto._
//import io.circe.syntax._
import io.circe.generic.JsonCodec

//@Lenses
@JsonCodec
case class EntityVersion(versionNumberLong: Long = 0 ) {

  def bumpVersion(): EntityVersion =
    this.copy( versionNumberLong = this.versionNumberLong + 1 )
}
