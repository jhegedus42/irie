package app.client.ui.caching.cache.comm.write

import app.client.ui.caching.cache.comm.AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import WriteRequestHandlerStates.{NotCalledYet, RequestError, RequestSuccess, WriteHandlerState}
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.read.ReadCache
import app.client.ui.caching.cache.comm.read.ReadCache.getAllUsersReqCache
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.postRequests.{CreateEntityReq, UpdateReq}
import app.shared.comm.{PostRequest, WriteRequest}
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.{RefToEntityByID, RefToEntityWithVersion}
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
  RT  <: WriteRequest,
  Req <: PostRequest[RT]]
    extends WriteRequestHandlerTC[RT, Req] {
  self: ReadCacheInvalidator[RT, Req] =>

  type WR = PostRequest[RT]

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  var requestHandlerState
    : WriteRequestHandlerStates.WriteHandlerState[Req] =
    NotCalledYet[Req]()

  def getState = requestHandlerState

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
  ): WriteHandlerState[Req] = {

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

          rci.invalidateReadCache()

          ReRenderer.triggerReRender()
        }

      }

    }

    def sendAJAXCall(): Unit = {
      sendPostAjaxRequest[Req](AjaxCallPar(par))
        .onComplete(ajaxReturnHandler)
    }

    requestHandlerState match {
      case NotCalledYet()                              => sendAJAXCall()
      case WriteRequestHandlerStates.RequestPending(_) => Unit
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

  trait UpdateReqUserCacheInvalidator
      extends ReadCacheInvalidator[WriteRequest, UpdateReq[User]] {
    self: WriteRequestHandlerTCImpl[WriteRequest, UpdateReq[User]] =>
    override def invalidateReadCache(): Unit = {


      val s: WriteHandlerState[UpdateReq[User]] = self.requestHandlerState

      s.getPar.foreach(par => {

        val r: RefToEntityWithVersion[User] =
          par.currentEntity.refToEntity

        val r1 = RefToEntityByID[User](r.entityIdentity)

        ReadCache.getLatestUserCache.invalidateEntry(r1)
      })

    }
  }

  // this is a TC instance

  implicit val userUpdater
    : WriteRequestHandlerTCImpl[WriteRequest, UpdateReq[User]]
      with UpdateReqUserCacheInvalidator =
    new WriteRequestHandlerTCImpl[WriteRequest, UpdateReq[User]]
    with UpdateReqUserCacheInvalidator

  object CreateEntityReq {

    trait ReadCacheInvalidatorForCreateEntityRequest
        extends ReadCacheInvalidator[WriteRequest, CreateEntityReq[User]] {
      override def invalidateReadCache(): Unit = {
        getAllUsersReqCache.clearCache()
      }
    }

    val createUserEntityReqHandler =
      new WriteRequestHandlerTCImpl[WriteRequest, CreateEntityReq[User]] with
      ReadCacheInvalidatorForCreateEntityRequest
  }

}
