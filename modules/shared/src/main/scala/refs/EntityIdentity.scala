package refs
//import io.circe.generic.JsonCodec
import monocle.macros.Lenses
//import io.circe.generic.auto._
//import io.circe.syntax._
import io.circe.generic.JsonCodec

/**
  * Created by joco on 28/04/2017.
  */

  @JsonCodec
  @Lenses
  case class EntityIdentity[V <: EntityType[V]](
    uuid: String = java.util.UUID.randomUUID().toString) {
    def stripType: EntityIdentityUntyped = EntityIdentityUntyped(uuid)
  }





