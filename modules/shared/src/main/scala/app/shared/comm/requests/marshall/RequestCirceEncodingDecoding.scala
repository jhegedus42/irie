package app.shared.comm.requests.marshall

import app.shared.comm.requests.Request
import io.circe.parser._
import io.circe.{Decoder, Error, _}



object RequestCirceEncodingDecoding {


  def decodeJSONContainingOptResToOptRes[Req <: Request](
      res: JSONContainingRequestResultOption
    )(
      implicit
      e: Decoder[Req#Res]
    ): Either[Error, Req#Res] = {
    val r: Either[Error, Req#Res] = decode( res.string )
    r
  }

  def decodeJSONToPar[Req <: Request](
      res: JSONContainingRequestPar
    )(
      implicit
      e: Decoder[Req#Par]
    ): Either[Error, Req#Par] = {
    val r: Either[Error, Req#Par] = decode( res.string )
    r
  }

  def encodeOptResToJSONContainingOptRes[Req <: Request](
      r: Option[Req#Res]
    )(
      implicit
      e: Encoder[Option[Req#Res]]
    ): JSONContainingRequestResultOption = {
    JSONContainingRequestResultOption( e.apply( r ).spaces4 )
  }

  def encodeParToJSON[Req <: Request](r: Req#Par )(implicit e: Encoder[Req#Par] ): JSONContainingRequestPar = {
    JSONContainingRequestPar( e.apply( r ).spaces4 )
  }

}
