package app.shared.comm.postRequests.marshall

import app.shared.comm.{PostRequest, PostRequestType}
import io.circe.parser._
import io.circe.{Decoder, Error, _}

object EncodersDecoders {

  def decodeResult[Req<: PostRequest[_]](
    res: ResultOptionAsJSON
  )(
    implicit
    e: Decoder[Req#ResT]
  ): Either[Error, Req#ResT] = {
    val r: Either[Error, Req#ResT] = decode(res.resultOptionAsJSON)
    r
  }

  def decodeParameters[RT<:PostRequestType, Req<: PostRequest[RT]](
    res: ParametersAsJSON
  )(
    implicit
    e: Decoder[Req#ParT]
  ): Either[Error, Req#ParT] = {
    val r: Either[Error, Req#ParT] = decode(res.parameters_as_json)
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
  def encodeResult[Req<: PostRequest[_]](
    r: Option[Req#ResT]
  )(
    implicit
    e: Encoder[Option[Req#ResT]]
  ): ResultOptionAsJSON = {
    ResultOptionAsJSON(e.apply(r).spaces4)
  }

  def encodeParameters[Req<: PostRequest[_]](
    r: Req#ParT
  )(
    implicit e: Encoder[Req#ParT]
  ): ParametersAsJSON = {
    ParametersAsJSON(e.apply(r).spaces4)
  }

}
