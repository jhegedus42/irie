package app.client.ui.caching.cache

import app.client.ui.caching.cache.AJAXCalls.{AjaxCallPar, DecodingSuccess}
import app.shared.comm.postRequests.GetEntityReq
import app.shared.comm.postRequests.GetEntityReq.GetEntityReqPar
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestUsers
import org.scalatest.AsyncFunSuite
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContextExecutor, Future}

class GetEntityAsyncRequestTest extends AsyncFunSuite {
  override implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def myAssert(ds:DecodingSuccess[GetEntityReq[User]], user:User)={
    val user_alice=ds.res.res.get.entityValue
    println(
      s"""
         |
         |---------------------------------------------
         |
         |GetEntityAsyncRequestTest - returned entity:
         |
         |
         |$user_alice
         |
         |
         |
         |---------------------------------------------
         |
       """.stripMargin)
    val incomingName=ds.res.res.get.entityValue.name

    assert(incomingName===user.name)
  }

  test( "get entity test" ) {
    val alice: Entity[User] = TestUsers.aliceEntity_with_UUID0

    val alicePar: GetEntityReqPar[User] =
      GetEntityReqPar( alice.refToEntity )

    val ajaxCallPar = AjaxCallPar[GetEntityReq[User]]( alicePar )

    println(
     s"""
        |vvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        |
        |val ajaxCallPar = AjaxCallPar[GetEntityPostRequest[User]]( alicePar )
        |
        |is
        |
        |$ajaxCallPar
        |
        |^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
      """.stripMargin)

    val res: Future[AJAXCalls.DecodingSuccess[GetEntityReq[User]]] =
      AJAXCalls.sendPostAjaxRequest( ajaxCallPar )

    res.map(myAssert(_,alice.entityValue))
  }

}
