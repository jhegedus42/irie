package app.client.ui.caching.cache.comm

import app.client.ui.caching.cache.comm.AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import app.client.ui.caching.cache.comm.WriteRequestHandlerStates.{NotCalledYet, RequestError, RequestSuccess, WriteHandlerState}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.{PostRequest, WriteRequest}
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}
//import cats.implicits._

trait WriteRequestHandler[
  RT  <: WriteRequest,
  Req <: PostRequest[RT]] {

  def executeRequest(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): Unit
}

/**
  *
  * This should be singleton.
  *
  */
trait WriteRequestHandlerImpl[
  RT  <: WriteRequest,
  Req <: PostRequest[RT]]
    extends WriteRequestHandler[RT, Req] { ReadCacheInvalidator =>
  type WR = PostRequest[RT]
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  var requestHandlerState
    : WriteRequestHandlerStates.WriteHandlerState[Req] =
    NotCalledYet[Req]()

  def getState = requestHandlerState

  def invalidateReadCache(): Unit

  /**
    * We implement this now without error handling.
    *
    * @return
    */
  override def executeRequest(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): Unit = {

    def ajaxReturnHandler(
      r: Try[AJAXCalls.PostAJAXRequestSuccessfulResponse[Req]]
    ): Unit = {
      r match {
        case err @ Failure(exception) =>
          requestHandlerState = RequestError(err.toString)
        // - we do not do serious error handling now
        case s @ Success(
              value: AJAXCalls.PostAJAXRequestSuccessfulResponse[Req]
            ) => {

          requestHandlerState = RequestSuccess[Req](par,s.value.res)

          invalidateReadCache()

          ReRenderer.triggerReRender()
        }

      }

    }

    def sendAJAXCall(): Unit = {
      sendPostAjaxRequest[Req](AjaxCallPar(par))
    }

    requestHandlerState match {
      case NotCalledYet()                             => sendAJAXCall()
      case WriteRequestHandlerStates.RequestPending() => Unit
      case arrived: WriteRequestHandlerStates.RequestArrived[Req] => {
        arrived match {
          case WriteRequestHandlerStates.RequestError(_) => Unit
          case WriteRequestHandlerStates.RequestSuccess(_,_) =>
            sendAJAXCall()
          case _ => Unit
        }
      }
    }

  }

}
