package app.client.ui.caching.cache.comm.write

import app.client.ui.caching.cache.comm.AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import WriteRequestHandlerStates.{NotCalledYet, RequestError, RequestSuccess, WriteHandlerState}
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.{PostRequest, WriteRequest}
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}
//import cats.implicits._







/**
  *
  * This should be singleton.
  *
  */
trait WriteRequestHandlerTCImpl[
// todo-now-5 write type class instance for User Update Post Request
//  this one : app.shared.comm.postRequests.UpdateReq
  RT  <: WriteRequest,
  Req <: PostRequest[RT]
] extends WriteRequestHandlerTC[RT, Req] {  ReadCacheInvalidator =>
  type WR = PostRequest[RT]
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  var requestHandlerState
    : WriteRequestHandlerStates.WriteHandlerState[Req] =
    NotCalledYet[Req]()

  def getState = requestHandlerState

  def invalidateReadCache(): Unit = ??? //todo-now-6
  //

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
  ): WriteHandlerState[Req]= {

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

          requestHandlerState = RequestSuccess[Req](par, s.value.res)

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
          case WriteRequestHandlerStates.RequestSuccess(_, _) =>
            sendAJAXCall()
          case _ => Unit
        }
      }
    }
  requestHandlerState
  }

}

object WriteRequestHandlerTCImpl {
  implicit val updateUserWriteHandler
    : WriteRequestHandlerTCImpl[WriteRequest, PostRequest[
      WriteRequest
    ]] =
    new WriteRequestHandlerTCImpl[WriteRequest, PostRequest[WriteRequest
    ]](){}

}
