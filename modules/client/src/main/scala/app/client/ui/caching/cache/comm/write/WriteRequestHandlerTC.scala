package app.client.ui.caching.cache.comm.write

import WriteRequestHandlerStates.WriteHandlerState
import app.shared.comm.{PostRequest, WriteRequest}
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag

trait WriteRequestHandlerTC[
  RT  <: WriteRequest,
  Req <: PostRequest[RT]] { self: WriteCacheInvalidator[RT, Req] =>

  val rci=self

  def executeRequest(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): WriteHandlerState[Req]
}

