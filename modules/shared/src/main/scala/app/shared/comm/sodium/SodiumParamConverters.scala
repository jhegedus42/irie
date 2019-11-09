package app.shared.comm.sodium

import app.shared.entity.entityValue.EntityType
import io.circe.parser.decode
import io.circe.{Decoder, Encoder}

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
