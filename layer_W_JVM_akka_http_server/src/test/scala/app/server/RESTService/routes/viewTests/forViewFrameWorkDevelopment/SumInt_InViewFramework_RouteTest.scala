//package app.server.RESTService.routes.viewTests.forViewFrameWorkDevelopment
//
//import akka.http.scaladsl.model.HttpRequest
//import app.comm_model_on_the_server_side.serverSide.logic.ServerSideLogic.ServerLogicTypeClass
//import app.comm_model_on_the_server_side.simple_route.{PairOfInts, SumIntViewRoute_For_Testing, SumOfInts}
//import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{
//  ViewHttpRouteName,
//  ViewHttpRouteNameProvider
//}
//import app.server.RESTService.HttpServer_For_ImageMemory_App
//import app.server.RESTService.mocks.TestServerFactory
//import app.server.RESTService.routes.RoutesTestBase
//import app.server.RESTService.routes.views.ViewRoute
//import app.server.persistence.ApplicationState
//import app.shared.data.model.LineText
//import app.shared.rest.routes.crudRequests.CreateEntityRequest
//import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject._
//import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//import io.circe.generic.auto._
//import io.circe.generic.auto._
//import io.circe._
//import io.circe.parser._
//import io.circe.syntax._
//import io.circe.{Decoder, Encoder}
//
///**
//  */
//// Random UUID: 720b43da73f04f42bda67cb50fa58078
//// commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
//// Date: Sun Sep  2 16:40:39 EEST 2018
//
//trait SumInt_InViewFramework_RouteTestTrait {
//  this: RoutesTestBase =>
//
//  type ViewToTest = SumIntView
//
//  "simple test" should {
//
//    "do a simple test" in {
//
////      val whatWeWantToSend                 = PairOfInts(2,3) // régi
//      val whatWeWantToSend = SumIntView_Par( 2, 3 )
//
////      val URLToWhereWeWantToSendTheRequest = "/getSumOfIntsView"
//      val routeName: ViewHttpRouteName =
//        ViewHttpRouteNameProvider.getViewHttpRouteName[ViewToTest]()
//
//      println(s"routeName: $routeName")
//
//      val URLToWhereWeWantToSendTheRequest: String =
//        routeName.getPathNameForClient
//
//      val req: HttpRequest =
//        Post( URLToWhereWeWantToSendTheRequest, whatWeWantToSend ) // 4a1485de473d47c08bc96d1f018b07bd$5e45b350c3d7df91abb31d34817ad48226d70ff8
//
//      val ent=req.entity.withoutSizeLimit()
//      println( "Post message:" + req.httpMessage )
//      println( "Post request:" + req )
//      println( "Post request ent:" + ent )
//
////      val route = SumIntViewRoute_For_Testing.route
//
//      val sl = implicitly[ServerLogicTypeClass[ViewToTest]]
//
//      val route = ViewRoute.getRouteForView[ViewToTest]() // Random UUID: d7b8aea40f454a46af529da21328f1aa
//
//      req ~> route ~> check {
//
//        val result: SumIntView_Res = responseAs[SumIntView_Res]
//
//        println( "response from Post request:" + result )
//
//        result shouldBe SumIntView_Res( 5 )
//
//      }
////      s.system.terminate()
//    }
//
//  }
//
//}
//
///**
//  *
//  */
//
//class SumIntInViewFrameworkRouteTest_Instance
//    extends RoutesTestBase with SumInt_InViewFramework_RouteTestTrait {
//  // this is the stuff that defines what needs to be tested
//
//  override def server(initState: ApplicationState ): HttpServer_For_ImageMemory_App =
//    TestServerFactory.getTestServer( initState )
//}
