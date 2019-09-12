package app.client.ui.caching.cache.comm

import app.client.ui.caching.cache.comm.AJAXCalls.{
  AjaxCallPar,
  PostAJAXRequestSuccessfulResponse
}
import app.shared.comm.PostRequest
import app.shared.comm.postRequests.{
  GetEntityReq,
  ResetRequest,
  UpdateReq
}
import app.shared.comm.postRequests.GetEntityReq.GetEntityReqPar
import app.shared.comm.postRequests.UpdateReq.UpdateReqPar
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion
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
    currentEntity: Entity[User],
    newValue:      User
  ): Future[Entity[User]] = {
    val p: UpdateReqPar[User] =
      UpdateReqPar[User](currentEntity, newValue)

    val par = AjaxCallPar[UpdateReq[User]](p)

    val ac
      : Future[PostAJAXRequestSuccessfulResponse[UpdateReq[User]]] =
      AJAXCalls.sendPostAjaxRequest(par)

    val res: Future[Entity[User]] = ac.map(_.res.entity)

    res
  }

  def setUsersFavNumber(
    entity: Entity[User],
    favNum: Int
  ): Future[Entity[User]] = {
    val newValue =
      entity.entityValue.lens(_.favoriteNumber).set(favNum)
    updateUser(entity, newValue)

  }

  def getUser(
    ref: RefToEntityWithoutVersion[User]
  ): Future[Entity[User]] = {

    val requestPar: GetEntityReqPar[User] =
      GetEntityReqPar(ref)

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

  def getPostReqResult[Req <: PostRequest](
    par: Req#Par
  )(
    implicit
    ct:        ClassTag[Req],
    classTag2: ClassTag[Req#PayLoad],
    encoder:   Encoder[Req#Par],
    decoder:   Decoder[Req#Res]
  ): Future[Req#Res] = {
    val ajaxPar = AjaxCallPar(par)
    val res: Future[PostAJAXRequestSuccessfulResponse[Req]] =
      AJAXCalls.sendPostAjaxRequest[Req](ajaxPar)
    val resStripped: Future[Req#Res] = res.map(_.res)
    resStripped
  }

  def isUsersFavoriteNumberX(
    ref:       RefToEntityWithoutVersion[User],
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
    resultingEntity: Entity[User],
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