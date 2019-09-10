package app.client.ui.caching.cache.comm

import app.client.ui.caching.cache.comm.AJAXCalls.{AjaxCallPar, PostAJAXRequestSuccessfulResponse}
import app.shared.comm.postRequests.{GetEntityReq, UpdateReq}
import app.shared.comm.postRequests.GetEntityReq.GetEntityReqPar
import app.shared.comm.postRequests.UpdateReq.UpdateReqPar
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import io.circe.generic.auto._
import org.scalatest.{Assertion, AsyncFunSuite}

import scala.concurrent.{ExecutionContextExecutor, Future}

case class AsyncRequestTestHelper(
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

  def getUser(entity: Entity[User]): Future[Entity[User]] = {

    val requestPar: GetEntityReqPar[User] =
      GetEntityReqPar(entity.refToEntity.stripVersion())

    val ajaxCallPar = AjaxCallPar[GetEntityReq[User]](requestPar)

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
