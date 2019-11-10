package syncedNormalizedState.comm

import com.sun.javafx.scene.layout.region.Margins.Converter
import dataModel.EntityValueType
import io.circe.parser.decode
import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

trait SodiumParamConverters[
  Req <: SodiumCRUDReq[V],
  V   <: EntityValueType[V]] {

  implicit def decoder : Decoder[Req#Par] = implicitly[Decoder[Req#Par]]
  implicit def encoder: Encoder[Req#Resp] = implicitly[Encoder[Req#Resp]]
  case class Converters() {


    import io.circe.syntax._
    import io.circe.generic.auto._
    import io.circe.generic.JsonCodec

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
    ): Req#Resp = decode(string)(decoder).toOption.get

    implicit def stringToPar(
      string: String
    )(
      implicit decoder: Decoder[Req#Par]
    ): Req#Par = decode(string)(decoder).toOption.get

  }

  lazy val converters = Converters()

}
