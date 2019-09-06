package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSuite
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.state.TestStateProvider
import app.shared.comm.RouteName
import app.shared.comm.postRequests.{GetEntityRoute, InsertNewEntityRoute}
import app.shared.comm.postRequests.GetEntityRoute.GetEntityReqRes
import app.shared.comm.postRequests.InsertNewEntityRoute.InsertReqRes
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
import io.circe.generic.auto._

class RouteFactoryTest extends FunSuite with Matchers with ScalatestRouteTest {
  val as: ActorSystem = ActorSystem()

  val routes = RouteFactory(as)
  test("testsimplePostRouteHelloWorldRoute") {
    Post("/hello_world") ~> routes.route ~> check {
      responseAs[String] shouldEqual "Hello world !"
    }
  }

  test("test insert route[User]") {

    val rn: String = "/" + RouteName
      .getRouteName[InsertNewEntityRoute[User]]()
      .name

//    val
    import io.circe.parser._
    import io.circe.{Decoder, Encoder, Error, _}

    val mhb = TestUsers.meresiHiba
    val par: InsertNewEntityRoute.InsertReqPar[User] =
      InsertNewEntityRoute.InsertReqPar(mhb)

    val json: ParametersAsJSON =
      EncodersDecoders.encodeParameters[InsertNewEntityRoute[User]](par)

    val json_par_as_string: String = json.parameters_as_json

    val req = Post(rn).withEntity(json_par_as_string)

    var resp: String = null

    req ~> routes.route ~> check {
      val r=responseAs[String]
      resp=r
      true
    }

      println(resp)

      val ent: Entity[User] = EncodersDecoders
        .decodeResult[InsertNewEntityRoute[User]](
          ResultOptionAsJSON(resp)
        )
        .right
        .get.entity


    testGetEntity(ent)
    }


  test("test update route"){
    // todo-now-0 write an akka-http-test for the update route



  }


  def testGetEntity(entity: Entity[User]): Unit = {
    val rn: String = "/" + RouteName.getRouteName[GetEntityRoute[User]]().name
    val refToAlice_withVersion: RefToEntityWithVersion[User] =
      entity.refToEntity

    val refToAlice_withoutVersion: RefToEntityWithoutVersion[User] =
      refToAlice_withVersion.stripVersion()
    val par = GetEntityRoute.GetEntityReqPar(refToAlice_withoutVersion)

    val json: ParametersAsJSON =
      EncodersDecoders.encodeParameters[GetEntityRoute[User]](par)

    val json_par_as_string: String = json.parameters_as_json

    val req = Post(rn).withEntity(json_par_as_string)

    // get User route
    val expected: String = {
      val res:  Option[Entity[User]]          = Some(entity)
      val res2: Option[GetEntityReqRes[User]] = Some(GetEntityReqRes(res))
      val asJSON: ResultOptionAsJSON =
        EncodersDecoders.encodeResult[GetEntityRoute[User]](res2)
      asJSON.resultOptionAsJSON
    }

    req ~> routes.route ~> check {
      responseAs[String] shouldEqual expected
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
