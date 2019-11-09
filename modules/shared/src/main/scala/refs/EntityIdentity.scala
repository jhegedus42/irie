package refs
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

/**
  * Created by joco on 28/04/2017.
  */

@JsonCodec
case class EntityIdentity[V <: EntityType[V]](
    uuid: String = java.util.UUID.randomUUID().toString) {
    def stripType: EntityIdentityUntyped = EntityIdentityUntyped(uuid)
}





