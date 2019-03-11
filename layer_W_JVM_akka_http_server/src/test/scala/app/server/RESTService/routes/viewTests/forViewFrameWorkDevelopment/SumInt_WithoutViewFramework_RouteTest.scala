//package app.server.RESTService.routes.viewTests.forViewFrameWorkDevelopment
//
//import akka.http.scaladsl.model.HttpRequest
//import app.comm_model_on_the_server_side.simple_route.{PairOfInts, SumIntViewRoute_For_Testing, SumOfInts}
//import app.server.RESTService.HttpServer_For_ImageMemory_App
//import app.server.RESTService.mocks.TestServerFactory
//import app.server.RESTService.routes.RoutesTestBase
//import app.server.persistence.ApplicationState
//import app.shared.data.model.LineText
//import app.shared.rest.routes.crudRequests.CreateEntityRequest
//
///**
//  * Does not use the view framework.
//  * // Random UUID: 5cc95ad5f0b7486785c066b591c81a06
//  * // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
//  * // Date: Sat Sep  1 20:27:16 EEST 2018
//  */
//
//trait SumInt_RouteTestTrait {
//  this: RoutesTestBase =>
//
//  type ResCET = CreateEntityRequest[LineText]#Result
//  val cec = CreateEntityRequest[LineText]
//
//
//  "simple test" should {
//
//    "do a simple test" in {
//      // Random UUID: 52696128b5244535afb18db8e0b124d3
//      // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
//      // Date: Sat Aug 11 14:32:17 EEST 2018
//
//      import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//      import io.circe.generic.auto._
//
//      val whatWeWantToSend                 = PairOfInts(2,3)
//      val URLToWhereWeWantToSendTheRequest = "/getSumOfIntsView"
//      val req: HttpRequest = Post( URLToWhereWeWantToSendTheRequest, whatWeWantToSend )
//
//      println( "Post request:" + req )
//
//      // Random UUID: 10c93031616f4da381a38643eb8111d1
//      // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
//      // Date: Sat Sep  1 19:01:35 EEST 2018
//      val route = SumIntViewRoute_For_Testing.route
//
//
//      req ~> route ~> check {
//
//        // ^^^ ez kell ide hogy a valaszt tudjuk dekodolni a kovetkezo sorban
//
//        // Random UUID: a30b945c9b5045489e9c1eb00e318071
//        // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
//        // Date: Sat Sep  1 19:06:48 EEST 2018
//
//        val result: SumOfInts = responseAs[SumOfInts]
//
//        println( "response from Post request:" + result )
//
//        result shouldBe SumOfInts(5)
//
//
//        // aszondjuk itt, hogy
//        // vmi ilyesmit fogunk csinalni :
//        // req.toEither.right.get.v shouldBe line
////                                            assertion( result )
//
//
//
//      }
////      s.system.terminate()
//    }
//
//  }
//
//
//}
//
///**
//  *
//  */
//
//class SumIntRouteTest_Instance extends RoutesTestBase with SumInt_InViewFramework_RouteTestTrait {
//  // this is the stuff that defines what needs to be tested
//
//  override def server(initState: ApplicationState ): HttpServer_For_ImageMemory_App =
//    TestServerFactory.getTestServer( initState )
//}
