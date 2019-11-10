package refs

//import io.circe.generic.JsonCodec
//import io.circe.generic.auto._
//import io.circe.syntax._
import dataModel.EntityType
import io.circe.generic.JsonCodec
import shapeless.T

//@Lenses
@JsonCodec
case class Version[V<:EntityType[T]](versionNumberLong: Long = 0 ) {

  def bumpVersion(): Version[V] =
    this.copy( versionNumberLong = this.versionNumberLong + 1 )
}
