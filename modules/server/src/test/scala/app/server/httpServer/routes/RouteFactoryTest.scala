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
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.{
  RefToEntityWithVersion,
  RefToEntityWithoutVersion
}
import app.shared.initialization.testing.TestUsers
import io.circe
import io.circe.generic.auto._
import monocle.macros.syntax.lens._
import scala.reflect.ClassTag

class RouteFactoryTest
    extends FunSuite
    with Matchers
    with ScalatestRouteTest {
  val as: ActorSystem = ActorSystem()

  val routes = RouteFactory(as)
  test("testsimplePostRouteHelloWorldRoute") {
    Post("/hello_world") ~> routes.route ~> check {
      responseAs[String] shouldEqual "Hello world !"
    }
  }

  import io.circe.{Decoder, Error, _}

  def getPostRequestResult[Req <: PostRequest, V <: EntityValue[
    V
  ]](
      par: Req#Par
  )(
      implicit
      encoder: Encoder[Req#Res],
      decoder: Decoder[Req#Res],
      enc_par: Encoder[Req#Par],
      e2:      Encoder[Entity[V]],
      ct1:     ClassTag[Req#PayLoad],
      ct2:     ClassTag[Req]
  ): Req#Res = {

    val rn: String = "/" + RouteName
      .getRouteName[Req]()
      .name

    val encodedPars: ParametersAsJSON = encodeParameters(par)

    val req = Post(rn).withEntity(
      encodedPars.parameters_as_json
    )

    var resp: String = null

    req ~> routes.route ~> check {
      val r = responseAs[String]
      resp = r
      true
    }

    println(resp)

    val res: Req#Res =
      decodeResult[Req](
        ResultOptionAsJSON(resp)
      ).toOption.get

    res

  }

  def executeUpdateUserRequest(
      currentEntity: Entity[User],
      newValue:      User
  ): Entity[User] = {

    val rn: String = "/" + RouteName
      .getRouteName[UpdateEntityRoute[User]]()
      .name

    val par: UpdateEntityRoute.UpdateReqPar[User] =
      UpdateEntityRoute
        .UpdateReqPar[User](currentEntity, newValue)

    val json: ParametersAsJSON =
      encodeParameters[UpdateEntityRoute[User]](par)

    val json_par_as_string: String = json.parameters_as_json

    val req = Post(rn).withEntity(json_par_as_string)

    var resp: String = null

    req ~> routes.route ~> check {
      val r = responseAs[String]
      resp = r
      true
    }

    println(resp)

    val resDecoded: Either[
      circe.Error,
      UpdateEntityRoute.UpdateReqRes[User]
    ] =
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

    val ent: Entity[User] =
      decodeResult[InsertNewEntityRoute[User]](
        ResultOptionAsJSON(resp)
      ).right.get.entity

    ent

  }

  test("test insert route[User]") {

    val mhb = TestUsers.meresiHiba

    val insertedEntity = executeInsertUserRequest(mhb)

    assertLatestEntityIs(insertedEntity)

  }

  def getLatestEntity[V <: EntityValue[V]](
      ref: RefToEntityWithoutVersion[V]
  )(
      implicit
      encoder: Encoder[GetEntityReq[V]#Res],
      decoder: Decoder[GetEntityReq[V]#Res],
      enc_ent: Encoder[Entity[V]],
      ct1:     ClassTag[GetEntityReq[V]#PayLoad]
  ): Entity[V] = {
    val rn: String = "/" + RouteName
      .getRouteName[GetEntityReq[User]]()
      .name

    val par: GetEntityReqPar[V] =
      GetEntityReqPar(ref)

    val res: GetEntityReqRes[V] =
      getPostRequestResult[GetEntityReq[V], V](par)

    val entity = res.optionEntity.get
    entity
  }

  def assertLatestEntityValueIs[V <: EntityValue[V]](
      ref: RefToEntityWithoutVersion[V],
      ev:  EntityValue[V]
  ): Unit = {}

  test("test update route") {

    val ta = TestUsers.terezAnya

    val insertedEntity = executeInsertUserRequest(ta)

    val refWOVersion: RefToEntityWithoutVersion[User] =
      insertedEntity.refToEntity.stripVersion()

    assertLatestEntityIs(insertedEntity)

    val currentEntity: Entity[User] = insertedEntity

    val currentEntityiValue: User =
      currentEntity.entityValue

    val newEntityValue: User =
      currentEntityiValue.lens(_.favoriteNumber).set(7)

    val res: Entity[User] =
      executeUpdateUserRequest(currentEntity, newEntityValue)

    assertLatestEntityIs(res)

    val latestEntity: Entity[User] = getLatestEntity(refWOVersion)

    val newFavoriteNumber = res.entityValue.favoriteNumber

    assert(newFavoriteNumber===latestEntity.entityValue.favoriteNumber)

    // todo-now - run this test and make it pass :)

    // update it
    // read it
    // assert it

  }

  /**
    * Assert that given entity is in the servers database and
    * the newest version that the server has is the same
    * that we have.
    *
    * @param entity
    */
  def assertLatestEntityIs(
      entity: Entity[User]
  ): Unit = {
    val rn: String = "/" + RouteName
      .getRouteName[GetEntityReq[User]]()
      .name

    val req = Post(rn).withEntity(
      encodeParameters[GetEntityReq[User]](
        GetEntityReqPar(entity.refToEntity.stripVersion())
      ).parameters_as_json
    )

    val expectedResponse: String = {
      val r: Option[GetEntityReqRes[User]] = Some(
        GetEntityReqRes(Some(entity))
      )
      encodeResult[GetEntityReq[User]](r).resultOptionAsJSON
    }

    req ~> routes.route ~> check {
      responseAs[String] shouldEqual expectedResponse
    }

  }

  test("test get route[User]") {

//    val
    import io.circe.parser._
    import io.circe.{Decoder, Encoder, Error, _}

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
