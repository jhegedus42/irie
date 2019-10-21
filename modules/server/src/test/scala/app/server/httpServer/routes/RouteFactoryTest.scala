package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSuite
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{HttpMessage, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.state.TestDataProvider
import app.shared.comm.{PostRequest, RouteName}
import app.shared.comm.postRequests.{CreateEntityReq, GetEntityReq, GetUsersNotesReq, LoginReq, ResetRequest, UpdateReq}
import app.shared.comm.postRequests.GetEntityReq._
import app.shared.comm.postRequests.CreateEntityReq.CreateEntityReqRes
import app.shared.comm.postRequests.marshall.JSONEncodersDecoders._
import app.shared.comm.postRequests.marshall.{JSONEncodersDecoders, ParametersAsJSON, ResultOptionAsJSON}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestEntitiesForUsers
import io.circe.generic.auto._
import monocle.macros.syntax.lens._

//package implTest {
//  case class Macska(name:                    String)
//  case class Implicits[V]()(implicit macska: Macska) {
//
//    implicit val m = macska
//
//  }
//
//  object Implicits {
//    implicit val bela: Implicits[User] =
//      Implicits[User]()(Macska("bela"))
//  }
//}

class RouteFactoryTest
    extends FunSuite
    with Matchers
    with ScalatestRouteTest {
  val as: ActorSystem = ActorSystem()

  val routes: RouteFactory = RouteFactory(as)

  val testHelper = TestHelper(routes)

  import testHelper._

  test("testsimplePostRouteHelloWorldRoute") {
    Post("/hello_world") ~> routes.route ~> check {
      responseAs[String] shouldEqual "Hello world !"
    }
  }

  test("test insert route[User]") {

    resetServerState()
    val mhb = TestEntitiesForUsers.jetiLabnyom

    val insertedEntity = executeInsertUserRequest(mhb)

    assertLatestEntityIs(insertedEntity)

  }

  test("get user note list"){
    val a= TestEntitiesForUsers.aliceEntity_with_UUID0
    import GetUsersNotesReq._
    val par:Par =Par(a.refToEntity.entityIdentity)

  }

  test("test update route") {

    // we insert a new Terez Anya

    resetServerState()
    val terezAnyaValue = TestEntitiesForUsers.terezAnya

    val originalTA =
      executeInsertUserRequest(terezAnyaValue)

    // we check that she is inserted:

    assertLatestEntityIs(originalTA)

    // we update her favorite number:

    val updatedTAValue: User =
      originalTA.entityValue
        .lens(_.favoriteNumber)
        .set(7)

    val updatedTA: EntityWithRef[User] =
      executeUpdateUserRequest(originalTA, updatedTAValue)

    assertLatestEntityIs(updatedTA)

    val latestEntity = getEntity(updatedTA.refToEntity)

    assert(
      updatedTA.entityValue.favoriteNumber === latestEntity.entityValue.favoriteNumber
    )

  }

  test("login route") {


    assert(
      getPostRequestResult[LoginReq](
        LoginReq.Par("Alice", "titokNyitja")
      ).optionUserRef.get ===
        TestEntitiesForUsers.aliceEntity_with_UUID0
    )


    assert(
      getPostRequestResult[LoginReq](
        LoginReq.Par("Alice", "titokNyitj")
      ).optionUserRef === None
    )

  }

  test("test get route[User]") {

//    val

    resetServerState()
    val alice: EntityWithRef[User] =
      TestEntitiesForUsers.aliceEntity_with_UUID0
    val refToEntityWithVersion = alice.refToEntity

    assertLatestEntityIs(alice)

  }

  test("ping_pong") {
    Post("/ping_pong").withEntity("hello") ~> routes.route ~> check {
      responseAs[String] shouldEqual "hello"
    }

  }

  test("test reset route") {

    resetServerState()

    val mhb: EntityWithRef[User] = TestEntitiesForUsers.meresiHiba_with_UUID2

    assertUserFavoriteNumber(mhb, 369)

    val mhb2 = mhb.bumpVersion

    updateUsersFavoriteNumer(mhb, 69)

    assertUserFavoriteNumber(mhb2, 69)

//    assertUserFavoriteNumber(mhb, 369)

    resetServerState()

    assertUserFavoriteNumber(mhb, 369)

    updateUsersFavoriteNumer(mhb, 99)

    assertUserFavoriteNumber(mhb2, 99)

  }

}
