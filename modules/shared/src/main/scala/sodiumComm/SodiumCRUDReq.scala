package sodiumComm

import refs.EntityWithRef
import refs.entityValue.EntityType
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import io.circe.parser._
import io.circe.{Decoder, Error, _}

sealed trait SodiumCRUDReq[V <: EntityType[V]] {

  type Par

  type Resp

}
case class GetAllLatestEntities[V <: EntityType[V]]()
    extends SodiumCRUDReq[V] {

  type Par = GetAllLatestEntities.Par

  type Resp= GetAllLatestEntities.Resp[V]

}

object GetAllLatestEntities {
  @JsonCodec
  case class Par()
  @JsonCodec
  case class Resp[V<:EntityType[V]](resp:Option[Set[EntityWithRef[V]]])
}




