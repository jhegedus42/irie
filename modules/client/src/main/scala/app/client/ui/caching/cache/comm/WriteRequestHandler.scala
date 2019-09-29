package app.client.ui.caching.cache.comm

import app.client.ui.caching.cache.comm.WriteRequestHandlerStates.NotCalledYet
import app.shared.comm.{PostRequest, WriteRequest}
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag
//import cats.implicits._

trait WriteRequestHandler[
  RT  <: WriteRequest,
  Req <: PostRequest[RT]] {

  def executeRequest = ???

}

/**
  *
  * This should be singleton.
  *
  */
case class WriteRequestHandlerImpl[
  RT  <: WriteRequest,
  Req <: PostRequest[RT]
]() extends WriteRequestHandler[RT,Req] {
  type WR = PostRequest[RT]
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  var requestHandlerState
    : WriteRequestHandlerStates.WriteHandlerState[Req] =
    NotCalledYet[Req]()

  /**
    * We implement this now without error handling.
    *
    * @return
    */
  def executeRequest(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ) = ???

}
