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
import app.shared.comm.RouteName
import app.shared.comm.postRequests.{
  GetEntityRoute,
  InsertNewEntityRoute,
  UpdateEntityRoute
}
import app.shared.comm.postRequests.GetEntityRoute._
import app.shared.comm.postRequests.InsertNewEntityRoute.InsertReqRes
import app.shared.comm.postRequests.marshall.EncodersDecoders._
import app.shared.comm.postRequests.marshall.{
  EncodersDecoders,
  ParametersAsJSON,
  ResultOptionAsJSON
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.{
  RefToEntityWithVersion,
  RefToEntityWithoutVersion
}
import app.shared.initialization.testing.TestUsers
import io.circe
import io.circe.generic.auto._

class RouteFactoryTest extends FunSuite with Matchers with ScalatestRouteTest {
  val as: ActorSystem = ActorSystem()

  val routes = RouteFactory(as)
  test("testsimplePostRouteHelloWorldRoute") {
    Post("/hello_world") ~> routes.route ~> check {
      responseAs[String] shouldEqual "Hello world !"
    }
  }

  def executeUpdateUserRequest(
      currentEntity: Entity[User],
      newValue:      User
  ): Entity[User] = {

    val rn: String = "/" + RouteName
      .getRouteName[UpdateEntityRoute[User]]()
      .name

//    val par: InsertNewEntityRoute.InsertReqPar[User] =
//      InsertNewEntityRoute.InsertReqPar(mhb)
    val par: UpdateEntityRoute.UpdateReqPar[User] =
      UpdateEntityRoute.UpdateReqPar[User](currentEntity, newValue)

//    val json: ParametersAsJSON =
//      encodeParameters[InsertNewEntityRoute[User]](par)

    val json: ParametersAsJSON =
      encodeParameters[UpdateEntityRoute[User]](par)

    val json_par_as_string: String = json.parameters_as_json

    val req = Post(rn).withEntity(json_par_as_string)

    //    req

    var resp: String = null

    req ~> routes.route ~> check {
      val r = responseAs[String]
      resp = r
      true
    }

    println(resp)

    val resDecoded: Either[circe.Error, UpdateEntityRoute.UpdateReqRes[User]] =
      decodeResult[UpdateEntityRoute[User]](
        ResultOptionAsJSON(resp)
      )

    val resUnsafeExtracted: UpdateEntityRoute.UpdateReqRes[User] =
      resDecoded.right.get

    val returnedEntity = resUnsafeExtracted.entity

    returnedEntity

  }

  def executeInsertUserRequest(u: User): Entity[User] = {

    val rn: String = "/" + RouteName
      .getRouteName[InsertNewEntityRoute[User]]()
      .name

    val mhb: User = u

    val par: InsertNewEntityRoute.InsertReqPar[User] =
      InsertNewEntityRoute.InsertReqPar(mhb)

    val json: ParametersAsJSON =
      encodeParameters[InsertNewEntityRoute[User]](par)

    val json_par_as_string: String = json.parameters_as_json

    val req = Post(rn).withEntity(json_par_as_string)

//    req

    var resp: String = null

    req ~> routes.route ~> check {
      val r = responseAs[String]
      resp = r
      true
    }

    println(resp)

    val ent: Entity[User] = decodeResult[InsertNewEntityRoute[User]](
      ResultOptionAsJSON(resp)
    ).right.get.entity

    ent

  }

  test("test insert route[User]") {

    val mhb = TestUsers.meresiHiba

    val insertedEntity = executeInsertUserRequest(mhb)

    testGetEntity(insertedEntity)

  }

  test("test update route") {

    // todo-now-0 write an akka-http-test for the update route

    val ta = TestUsers.terezAnya

    val insertedEntity = executeInsertUserRequest(ta)

    testGetEntity(insertedEntity)

    // update it // uj kedvenc szam // todo-now

    // todo-now : use executeUpdateUserRequest()
    //  needs a new value and current entity
    //  current entity is insertedEntity
    //  new value : we have to make that from `ta`

    // read it
    // assert it

    // update it
    // read it
    // assert it
    ???

  }

  /**
    * Assert that given entity is in the servers database and
    * the newest version that the server has is the same
    * that we have.
    *
    * @param entity
    */
  def testGetEntity(entity: Entity[User]): Unit = {
    val rn: String = "/" + RouteName.getRouteName[GetEntityRoute[User]]().name

    val req = Post(rn).withEntity(
      encodeParameters[GetEntityRoute[User]](
        GetEntityReqPar(entity.refToEntity.stripVersion())
      ).parameters_as_json
    )

    val expectedResponse: String = {
      val r: Option[GetEntityReqRes[User]] = Some(GetEntityReqRes(Some(entity)))
      encodeResult[GetEntityRoute[User]](r).resultOptionAsJSON
    }

    req ~> routes.route ~> check {
      responseAs[String] shouldEqual expectedResponse
    }

  }

  test("test get route[User]") {

//    val
    import io.circe.parser._
    import io.circe.{Decoder, Encoder, Error, _}

    val alice = TestUsers.aliceEntity_with_UUID0

    testGetEntity(alice)

  }

  test("ping_pong") {
    Post("/ping_pong").withEntity("hello") ~> routes.route ~> check {
      responseAs[String] shouldEqual "hello"
    }

  }

}
