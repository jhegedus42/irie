package app.client.ui.caching.cache.comm

import app.client.ui.caching.cache.comm.AJAXCalls.{
  AjaxCallPar,
  PostAJAXRequestSuccessfulResponse
}
import app.shared.comm.postRequests.InsertReq.InsertReqPar
import app.shared.comm.postRequests.UpdateReq.UpdateReqPar
import app.shared.comm.postRequests.{
  AdminPassword,
  GetAllUsersReq,
  InsertReq,
  ResetRequest,
  UpdateReq
}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestEntities
import io.circe.generic.auto._
import org.scalatest.{Assertion, AsyncFunSuite}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.Try

class AsyncRequestTest extends AsyncFunSuite {
  override implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  val helper = AsyncRequestTestHelper()

  test("getentity test") {
    println("now starting get test...")
    val alice: EntityWithRef[User] = TestEntities.aliceEntity_with_UUID0

    val r1: Future[EntityWithRef[User]] = helper
      .getUser(alice.refToEntity)

//    r1.onComplete(
//      (x: Try[Entity[User]]) =>
//        s"get user has returned with:${x.get.entityValue}"
//    )

    val res: Future[Assertion] =
      r1.map(
        (u: EntityWithRef[User]) =>
          helper.assertUserNamesAreEqual(u, alice.entityValue)
      )
    res
  }

  test("insert and then get") {
    println("now starting insert test...")
    val c = TestEntities.cica

    val par = AjaxCallPar[InsertReq[User]](InsertReqPar(c))

    val ac
      : Future[PostAJAXRequestSuccessfulResponse[InsertReq[User]]] =
      AJAXCalls.sendPostAjaxRequest(par)

    val insertResult: Future[EntityWithRef[User]] = ac.map(_.res.entity)

    val getResult: Future[EntityWithRef[User]] =
      insertResult.flatMap(
        (e: EntityWithRef[User]) =>
          helper.getUser(e.refToEntity)
      )

    val futureAssertionThatEverythingIsKosher: Future[Assertion] =
      getResult.map(
        (e: EntityWithRef[User]) => helper.assertUserNamesAreEqual(e, c)
      )
    futureAssertionThatEverythingIsKosher
  }

  test("updateEntity and then get") {
    println("now starting update test...")

    import monocle.macros.syntax.lens._

    val userEntity = TestEntities.meresiHiba_with_UUID2

    val newValue: User =
      userEntity.entityValue.lens(_.favoriteNumber).set(66)

    val updateReqResult: Future[EntityWithRef[User]] =
      helper.updateUser(userEntity, newValue)

    val getRequestResult: Future[EntityWithRef[User]] =
      updateReqResult.flatMap(
        e => helper.getUser(e.refToEntity)
      )
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

  test("basic test for reset state route ") {
    // just test that it executes "fine" and returns some meaningful
    // result
    println("now starting reset test...")
    val res: Future[ResetRequest.Res] = helper.resetServer()
    res.map(r => assert(r.message === "Minden kiraly!"))
  }

  test("reset, get, update, reset and get") {

    println("this test is to be implemented")
    val alicesOriginalFavNumber = TestEntities.alice.favoriteNumber

    // reset the server

    val reset: Future[ResetRequest.Res] = helper.resetServer()

    // get alice and check that her state is resetted

    val refToAlice =
      TestEntities.aliceEntity_with_UUID0.refToEntity

    val alice: Future[EntityWithRef[User]] = helper.waitFor(reset) {
      helper.getUser(refToAlice)
    }

    val test1: Future[Boolean] = alice.map(
      a => a.entityValue.favoriteNumber == alicesOriginalFavNumber
    )

    // now we update Alice

    val newAlice = alice.flatMap(a => helper.setUsersFavNumber(a, 66))

    val fetchedNewAlice: Future[EntityWithRef[User]] = newAlice.flatMap(
      na => helper.getUser(na.refToEntity)
    )

    // now we check that her state has been updated

    val test2: Future[Boolean] =
      fetchedNewAlice.map(fna => fna.entityValue.favoriteNumber == 66)

    // now we reset the server

    val reset2: Future[ResetRequest.Res] = helper.waitFor(test2) {
      helper.resetServer()
    }

    // now we check that alice has a resetted favorite number

    val aliceAfterUpdateAndReset: Future[EntityWithRef[User]] =
      helper.waitFor(reset) {
        helper.getUser(refToAlice)
      }

    val test3: Future[Boolean] = aliceAfterUpdateAndReset.map(
      a => a.entityValue.favoriteNumber == alicesOriginalFavNumber
    )

    // now we combine the 3 tests into one single assert
    val res: Future[Assertion] = for {
      a <- test1
      b <- test2
      c <- test3
      fna <- fetchedNewAlice
      d = fna.entityValue.favoriteNumber != 67
    } yield (assert(a && b && c && d))
    res
  }

  test("getAllUserRefs") {


    helper.resetServer()

    // call it

    val users = helper.getPostReqResult[GetAllUsersReq](
      GetAllUsersReq.Par(AdminPassword("titoknyitja"))
    )

    val length: Future[Int] = users.map(res => res.allUserRefs.length)

//    val testDataLength=TestD

    length.map(l => assert(l === 3))

    // todo-later
    //  move app.server.httpServer.routes.post.routeLogicImpl.persistentActor.state.TestDataProvider
    //  to shared module
    //  and calculate from that directly that number 3 ^^^
    //  which is in this test ^^^

  }

}
