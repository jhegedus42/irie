package syncedNormalizedState.comm

import dataModel.EntityValueType
import entity.Entity
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import io.circe.parser._
import io.circe.{Decoder, Error, _}

sealed trait SodiumCRUDReq[V <: EntityValueType[V]] {

  type Par

  type Resp

}
case class GetAllLatestEntities[V <: EntityValueType[V]]()
    extends SodiumCRUDReq[V] {

  type Par = GetAllLatestEntities.Par

  type Resp= GetAllLatestEntities.Resp[V]

}

object GetAllLatestEntities {
  @JsonCodec
  case class Par()
  @JsonCodec
  case class Resp[V<:EntityValueType[V]](resp:Option[Set[Entity[V]]])
}




