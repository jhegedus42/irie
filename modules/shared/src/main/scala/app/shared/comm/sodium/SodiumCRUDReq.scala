package app.shared.comm.sodium

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.comm.{PostRequest, PostRequestType}
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



trait SodiumParamConverters[
  Req <: SodiumCRUDReq[V],
  V   <: EntityType[V]] {
  implicit def parToString(
    par: Req#Par
  )(
    implicit encoder: Encoder[Req#Par]
  ): String = encoder.apply(par).spaces2

  implicit def respToString(
    resp: Req#Resp
  )(
    implicit encoder: Encoder[Req#Resp]
  ): String = encoder.apply(resp).spaces2

  implicit def stringToResp(
    string: String
  )(
    implicit decoder: Decoder[Req#Resp]
  ): Req#Resp = decode(string).toOption.get

  implicit def stringToPar(
    string: String
  )(
    implicit decoder: Decoder[Req#Par]
  ): Req#Par = decode(string).toOption.get

}
