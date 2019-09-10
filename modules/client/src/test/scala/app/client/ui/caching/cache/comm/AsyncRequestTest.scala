package app.client.ui.caching.cache.comm

import app.client.ui.caching.cache.comm.AJAXCalls.{
  AjaxCallPar,
  PostAJAXRequestSuccessfulResponse
}
import app.shared.comm.postRequests.InsertReq.InsertReqPar
import app.shared.comm.postRequests.UpdateReq.UpdateReqPar
import app.shared.comm.postRequests.{InsertReq, UpdateReq}
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestUsers
import io.circe.generic.auto._
import org.scalatest.{Assertion, AsyncFunSuite}

import scala.concurrent.{ExecutionContextExecutor, Future}

class AsyncRequestTest extends AsyncFunSuite {
  override implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  val helper = AsyncRequestTestHelper(executionContext)

  test("get entity test") {
    val alice: Entity[User] = TestUsers.aliceEntity_with_UUID0

    helper
      .getUser(alice)
      .map(
        (u: Entity[User]) =>
          helper.assertUserNamesAreEqual(u, alice.entityValue)
      )
  }

  test("insert and then get") {
    val c = TestUsers.cica

    val par = AjaxCallPar[InsertReq[User]](InsertReqPar(c))

    val ac
      : Future[PostAJAXRequestSuccessfulResponse[InsertReq[User]]] =
      AJAXCalls.sendPostAjaxRequest(par)

    val insertResult: Future[Entity[User]] = ac.map(_.res.entity)

    val getResult: Future[Entity[User]] =
      insertResult.flatMap((e: Entity[User]) => helper.getUser(e))

    val futureAssertionThatEverythingIsKosher: Future[Assertion] =
      getResult.map(
        (e: Entity[User]) => helper.assertUserNamesAreEqual(e, c)
      )
    futureAssertionThatEverythingIsKosher
  }

  test("update and then get") {
    import monocle.macros.syntax.lens._

    val userEntity = TestUsers.meresiHiba_with_UUID2

    val newValue: User =
      userEntity.entityValue.lens(_.favoriteNumber).set(66)

    val updateReqResult: Future[Entity[User]] =
      helper.updateUser(userEntity, newValue)

    val getRequestResult: Future[Entity[User]] =
      updateReqResult.flatMap(e => helper.getUser(e))
    // here we are waiting first for the update request to return
    // and only after that we launch the getRequest

    getRequestResult.onComplete(
      x => println(s"getUser in update test completed with :\n$x")
    )

    val to_return: Future[Assertion] = for {
      r1 <- updateReqResult
      r2 <- getRequestResult

      a = r2.entityValue.favoriteNumber == 66
      b = r1.entityValue.favoriteNumber == 66
      c = r2.refToEntity.entityVersion.versionNumberLong == 1
    } yield (assert(a && b && c))

    to_return
  }

  test(" basic test for reset state route ") {
    // just test that it executes "fine" and returns some meaningful
    // result
    ???
  }

}
