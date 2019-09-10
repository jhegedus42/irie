package app.shared.comm.postRequests.marshall

import app.shared.comm.PostRequest
import io.circe.parser._
import io.circe.{Decoder, Error, _}

object EncodersDecoders {

  def decodeResult[Req <: PostRequest](
    res: ResultOptionAsJSON
  )(
    implicit
    e: Decoder[Req#Res]
  ): Either[Error, Req#Res] = {
    val r: Either[Error, Req#Res] = decode(res.resultOptionAsJSON)
    r
  }

  def decodeParameters[Req <: PostRequest](
    res: ParametersAsJSON
  )(
    implicit
    e: Decoder[Req#Par]
  ): Either[Error, Req#Par] = {
    val r: Either[Error, Req#Par] = decode(res.parameters_as_json)
    r
  }

  /**
    *
    * This is meant to be used by the server to
    * encode the result into JSON so that it can
    * be sent over the wire easily.
    *
    * @param r
    * @param e
    * @tparam Req
    * @return
    */
  def encodeResult[Req <: PostRequest](
    r: Option[Req#Res]
  )(
    implicit
    e: Encoder[Option[Req#Res]]
  ): ResultOptionAsJSON = {
    ResultOptionAsJSON(e.apply(r).spaces4)
  }

  def encodeParameters[Req <: PostRequest](
    r: Req#Par
  )(
    implicit e: Encoder[Req#Par]
  ): ParametersAsJSON = {
    ParametersAsJSON(e.apply(r).spaces4)
  }

}
