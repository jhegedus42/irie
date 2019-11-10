package syncedNormalizedState.comm

import dataModel.EntityValueType
import entity.Entity

import io.circe.parser._
import io.circe.{Decoder, Error, _}

import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

//@JsonCodec
sealed trait ParT

@JsonCodec
sealed trait SodiumCRUDReq[V <: EntityValueType[V]] {
  type Par <: ParT
  type Resp

}


@JsonCodec
case class GetAllLatestEntities[V <: EntityValueType[V]]()
    extends SodiumCRUDReq[V] {

  type Par = GetAllLatestEntities.Par[V]

  type Resp= GetAllLatestEntities.Resp[V]

}

object GetAllLatestEntities {

  @JsonCodec
  case class Par[V<:EntityValueType[V]]() extends ParT

  @JsonCodec
  case class Resp[V<:EntityValueType[V]](resp:Option[Set[Entity[V]]])
}




