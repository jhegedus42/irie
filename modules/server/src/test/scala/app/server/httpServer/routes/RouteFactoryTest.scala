package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSuite
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import app.shared.comm.RouteName
import app.shared.comm.postRequests.GetEntityRoute
import app.shared.entity.entityValue.values.User

class RouteFactoryTest extends FunSuite with Matchers with ScalatestRouteTest {
  val as: ActorSystem = ActorSystem()

  val routes = RouteFactory(as)
  test("testsimplePostRouteHelloWorldRoute") {
    Post("/hello_world") ~> routes.route ~> check {
      responseAs[String] shouldEqual "Hello world !"
    }

  }
  test("test crudRouteFactory.route[User]") {

    val rn: String =RouteName.getRouteName[GetEntityRoute[User]]().name
//    val

    val json_par_as_string: String = ???
    // continue-here

    val req= Post(rn).withEntity(json_par_as_string)
    // continue-here

    // get User route
    val expected : String = ??? // fill this in
    // continue-here

    req ~> routes.route ~> check {
      responseAs[String] shouldEqual expected
    }
  }

  test("ping_pong"){
    Post("/ping_pong").withEntity("hello") ~> routes.route ~> check {
      responseAs[String] shouldEqual "hello"
    }

  }

}
