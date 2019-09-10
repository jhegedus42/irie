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
import app.shared.comm.postRequests.{
  GetEntityReq,
  InsertReq,
  ResetRequest,
  UpdateReq
}
import app.shared.comm.postRequests.GetEntityReq._
import app.shared.comm.postRequests.InsertReq.InsertReqRes
import app.shared.comm.postRequests.marshall.EncodersDecoders._
import app.shared.comm.postRequests.marshall.{
  EncodersDecoders,
  ParametersAsJSON,
  ResultOptionAsJSON
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion
import app.shared.initialization.testing.TestUsers
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
    val mhb = TestUsers.jetiLabnyom

    val insertedEntity = executeInsertUserRequest(mhb)

    assertLatestEntityIs(insertedEntity)

  }

  test("test update route") {

    // we insert a new Terez Anya

    resetServerState()
    val terezAnyaValue = TestUsers.terezAnya

    val originalTA =
      executeInsertUserRequest(terezAnyaValue)

    // we check that she is inserted:

    assertLatestEntityIs(originalTA)

    // we update her favorite number:

    val updatedTAValue: User =
      originalTA.entityValue
        .lens(_.favoriteNumber)
        .set(7)

    val updatedTA: Entity[User] =
      executeUpdateUserRequest(originalTA, updatedTAValue)

    assertLatestEntityIs(updatedTA)

    assert(
      updatedTA.entityValue.favoriteNumber === getLatestEntity(
        originalTA.refToEntity.stripVersion()
      ).entityValue.favoriteNumber
    )

  }

  test("test get route[User]") {

//    val

    resetServerState()
    val alice: Entity[User] = TestUsers.aliceEntity_with_UUID0
    val refToEntityWithoutVersion = alice.refToEntity.stripVersion()

    assertLatestEntityIs(alice)

  }

  test("ping_pong") {
    Post("/ping_pong").withEntity("hello") ~> routes.route ~> check {
      responseAs[String] shouldEqual "hello"
    }

  }

  test("test reset route") {

    resetServerState()

    val mhb: Entity[User] = TestUsers.meresiHiba_with_UUID2

    assertUserFavoriteNumber(mhb,369)

    updateUsersFavoriteNumer(mhb,69)

    assertUserFavoriteNumber(mhb,69)

    resetServerState()

    assertUserFavoriteNumber(mhb,369)


  }

}
