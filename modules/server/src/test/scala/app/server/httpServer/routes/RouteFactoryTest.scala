package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSuite
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{HttpMessage, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.state.TestStateProvider
import app.shared.comm.{PostRequest, RouteName}
import app.shared.comm.postRequests.{
  GetEntityReq,
  InsertNewEntityRoute,
  UpdateEntityRoute
}
import app.shared.comm.postRequests.GetEntityReq._
import app.shared.comm.postRequests.InsertNewEntityRoute.InsertReqRes
import app.shared.comm.postRequests.marshall.EncodersDecoders._
import app.shared.comm.postRequests.marshall.{
  EncodersDecoders,
  ParametersAsJSON,
  ResultOptionAsJSON
}
import app.shared.entity.Entity
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

    val mhb = TestUsers.meresiHiba

    val insertedEntity = executeInsertUserRequest(mhb)

    assertLatestEntityIs(insertedEntity)

  }

  test("test update route") {

    // we insert a new Terez Anya

    val terezAnyaValue = TestUsers.terezAnya
    val insertedTerezAnyaEntity =
      executeInsertUserRequest(terezAnyaValue)

    // we check that she is inserted:

    assertLatestEntityIs(insertedTerezAnyaEntity)

    // we update her favorite number:

    val currentEntity: Entity[User] = insertedTerezAnyaEntity
    val currentEntityiValue: User =
      currentEntity.entityValue
    val newEntityValue: User =
      currentEntityiValue.lens(_.favoriteNumber).set(7)
    val res: Entity[User] =
      executeUpdateUserRequest(currentEntity, newEntityValue)

    // we check that the latest, updated Terez Anya
    // has the latest favorite number, this means
    // that the result of the update request
    // should agree with the latest version of Terez Anya
    // that was received from the server
    // this is done in the following line :

    assertLatestEntityIs(res)

    // we do again the same thing manually :

    val refWOVersion: RefToEntityWithoutVersion[User] =
      insertedTerezAnyaEntity.refToEntity.stripVersion()
    val latestEntity: Entity[User] = getLatestEntity(refWOVersion)
    val newFavoriteNumber = res.entityValue.favoriteNumber

    assert(
      newFavoriteNumber === latestEntity.entityValue.favoriteNumber
    )

    // todo-now - run this test and make it pass :)

    // todo-later - do some more updates, and maybe version checks, etc:
    //   update it
    //   read it
    //   assert it

  }

  test("test get route[User]") {

//    val

    val alice: Entity[User] = TestUsers.aliceEntity_with_UUID0
    val refToEntityWithoutVersion = alice.refToEntity.stripVersion()

    assertLatestEntityIs(alice)

  }

  test("ping_pong") {
    Post("/ping_pong").withEntity("hello") ~> routes.route ~> check {
      responseAs[String] shouldEqual "hello"
    }

  }

}
