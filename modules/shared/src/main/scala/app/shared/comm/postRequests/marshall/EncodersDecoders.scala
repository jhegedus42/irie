package app.shared.comm.postRequests.marshall

import app.shared.comm.PostRouteType
import io.circe.parser._
import io.circe.{Decoder, Error, _}

object EncodersDecoders {

  def decodeResult[Req <: PostRouteType](
      res: ResultOptionAsJSON
  )(
      implicit
      e: Decoder[Req#Res]
  ): Either[Error, Req#Res] = {
    val r: Either[Error, Req#Res] = decode(res.resultOptionAsJSON)
    r
  }

  def decodeParameters[Req <: PostRouteType](
      res: ParametersAsJSON
  )(
      implicit
      e: Decoder[Req#Par]
  ): Either[Error, Req#Par] = {
    val r: Either[Error, Req#Par] = decode(res.parameters_as_json)
    r
  }

  def encodeResult[Req <: PostRouteType](
      r: Option[Req#Res]
  )(
      implicit
      e: Encoder[Option[Req#Res]]
  ): ResultOptionAsJSON = {
    ResultOptionAsJSON(e.apply(r).spaces4)
  }

  def encodeParameters[Req <: PostRouteType](
      r:        Req#Par
  )(implicit e: Encoder[Req#Par]): ParametersAsJSON = {
    ParametersAsJSON(e.apply(r).spaces4)
  }

}
