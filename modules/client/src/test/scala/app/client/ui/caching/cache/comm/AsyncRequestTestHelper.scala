package app.client.ui.caching.cache.comm

import app.client.ui.caching.cache.comm.AJAXCalls.{AjaxCallPar, PostAJAXRequestSuccessfulResponse}
import app.shared.comm.PostRequest
import app.shared.comm.postRequests.{GetEntityReq, ResetRequest}
import app.shared.comm.postRequests.GetEntityReq.Par
import app.shared.comm.postRequests.write.UpdateReq
import app.shared.comm.postRequests.write.UpdateReq.UpdateReqPar
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import org.scalatest.{Assertion, AsyncFunSuite}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
import monocle.macros.syntax.lens._

case class AsyncRequestTestHelper(
)(
  implicit
  executionContextExecutor: ExecutionContextExecutor)
    extends AsyncFunSuite {

  def updateUser(
                  currentEntity: EntityWithRef[User],
                  newValue:      User
  ): Future[EntityWithRef[User]] = {
    val p: UpdateReqPar[User] =
      UpdateReqPar[User](currentEntity, newValue)

    val par = AjaxCallPar[UpdateReq[User]](p)

    val ac
      : Future[PostAJAXRequestSuccessfulResponse[UpdateReq[User]]] =
      AJAXCalls.sendPostAjaxRequest(par)

    val res: Future[EntityWithRef[User]] = ac.map(_.res.entity)

    res
  }

  def setUsersFavNumber(
                         entity: EntityWithRef[User],
                         favNum: Int
  ): Future[EntityWithRef[User]] = {
    val newValue =
      entity.entityValue.lens(_.favoriteNumber).set(favNum)
    updateUser(entity, newValue)

  }

  def getUser(
    ref: RefToEntityWithVersion[User]
  ): Future[EntityWithRef[User]] = {

    val requestPar: Par[User] =
      Par(ref)

    val ajaxCallPar: AjaxCallPar[GetEntityReq[User]] =
      AjaxCallPar[GetEntityReq[User]](requestPar)

    println(s"""
               |vvvvvvvvvvvvvvvvvvvvvvvvvvvvv
               |
               |val ajaxCallPar = AjaxCallPar[GetEntityPostRequest[User]]( requestPar )
               |
               |is
               |
               |$ajaxCallPar
               |
               |^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
      """.stripMargin)

    val res: Future[
      AJAXCalls.PostAJAXRequestSuccessfulResponse[GetEntityReq[User]]
    ] =
      AJAXCalls.sendPostAjaxRequest(ajaxCallPar)

    res.map(_.res.optionEntity.get)
  }

  def resetServer(): Future[ResetRequest.Res] = {
    val par: ResetRequest.Par = ResetRequest.Par()
    val res: Future[ResetRequest.Res] =
      getPostReqResult[ResetRequest](par)
    res
  }

  def getPostReqResult[Req <: PostRequest[_]](
    par: Req#ParT
  )(
    implicit
    ct:        ClassTag[Req],
    classTag2: ClassTag[Req#PayLoadT],
    encoder:   Encoder[Req#ParT],
    decoder:   Decoder[Req#ResT]
  ): Future[Req#ResT] = {
    val ajaxPar = AjaxCallPar(par)
    val res: Future[PostAJAXRequestSuccessfulResponse[Req]] =
      AJAXCalls.sendPostAjaxRequest[Req](ajaxPar)
    val resStripped: Future[Req#ResT] = res.map(_.res)
    resStripped
  }

  def isUsersFavoriteNumberX(
    ref:       RefToEntityWithVersion[User],
    favNumber: Int
  ): Future[Boolean] = {
    val u = getUser(ref)
    val b = u.map(uf => uf.entityValue.favoriteNumber == favNumber)
    b
  }

  def waitFor[T](
    toBeAwaited: Future[_]
  )(toStart:     => Future[T]
  ): Future[T] = {
    val res: Future[T] = toBeAwaited.flatMap(_ => toStart)
    res
  }

  def assertUserNamesAreEqual(
                               resultingEntity: EntityWithRef[User],
                               user:            User
  ): Assertion = {

    println(s"""
               |
               |---------------------------------------------
               |
               |GetEntityAsyncRequestTest - returned entity:
               |
               |
               |$resultingEntity
               |
               |
               |
               |Expected entity :
               |
               |$user
               |
               |
               |
               |---------------------------------------------
               |
       """.stripMargin)

    assert(resultingEntity.entityValue.name === user.name)
  }

}
