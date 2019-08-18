package app.shared.comm.views.marshall

import app.shared.comm.views.PostRequest
import io.circe.parser._
import io.circe.{Decoder, Error, _}



object ViewCirceEncodingDecoding {


  def decodeJSONContainingOptResToOptRes[V <: PostRequest](
      res: JSONContainingOptRes
    )(
      implicit
      e: Decoder[V#Res]
    ): Either[Error, V#Res] = {
    val r: Either[Error, V#Res] = decode( res.string )
    r
  }

  def decodeJSONToPar[V <: PostRequest](
      res: JSONContainingGetViewPar
    )(
      implicit
      e: Decoder[V#Par]
    ): Either[Error, V#Par] = {
    val r: Either[Error, V#Par] = decode( res.string )
    r
  }

  def encodeOptResToJSONContainingOptRes[V <: PostRequest](
      r: Option[V#Res]
    )(
      implicit
      e: Encoder[Option[V#Res]]
    ): JSONContainingOptRes = {
    JSONContainingOptRes( e.apply( r ).spaces4 )
  }

  def encodeParToJSON[V <: PostRequest](r: V#Par )(implicit e: Encoder[V#Par] ): JSONContainingGetViewPar = {
    JSONContainingGetViewPar( e.apply( r ).spaces4 )
  }

}
