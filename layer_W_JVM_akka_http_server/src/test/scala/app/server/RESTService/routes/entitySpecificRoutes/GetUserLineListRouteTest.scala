//package app.server.RESTService.routes.entitySpecificRoutes
//
//import akka.http.scaladsl.server.Route
//import app.server.RESTService.HttpServer_For_ImageMemory_App
//import app.server.RESTService.mocks.TestServerFactory
//import app.server.RESTService.routes.RoutesTestBase
//import app.server.RESTService.routes.generalCRUD.{GetAllEntityRouteTest, GetEntityRouteTest}
//import app.server.persistence.ApplicationState
//import app.shared.data.ref.RefVal
//import app.shared.data.model.UserLineList
//import app.shared.rest.routes.crudRequests.GetAllEntitiesRequest
////import app.shared.rest.routes_take3.viewCommands.UserLineListsViewCommand
//import app.testHelpersServer.state.TestData
//import app.testHelpersShared.data.{TestDataLabels, TestEntities, TestEntitiesForStateThree}
//
//import scala.collection.immutable.Seq
//import scalaz.Alpha.E
//
///**
//  * Created by joco on 14/12/2017.
//  */
//trait GetUserLineListRouteTest {
//  this: RoutesTestBase =>
//
//  "get user line lists route" should {
//    "work just fine" in {
//      import io.circe.generic.auto._
//
//      val s: HttpServer_For_ImageMemory_App = server(TestData.getTestDataFromLabels(TestDataLabels.LabelThree))
////      val entities: Seq[RefVal[LineText]] = getAllEntitiesHelper( s, EntityType.make[LineText] )
//
//      val resBe:  Seq[RefVal[UserLineList]] = List(TestEntitiesForStateThree.listRV)
//
//      val resNotBe: Seq[RefVal[UserLineList]] =  List( )
//
//      // assert that we got all lines
//      // 5587d5c97cc1457d8b629962b5ed30c4
//
//      val r:   Route  = s.route
////      val com = UserLineListsViewCommand()
////      val url: String = com.queryURL(TestEntitiesForStateThree.userRef)
////      type Res = com.Result
//
////      println("url:"+url)
//
////      val entities: List[RefVal[UserLineList]] = Get(url) ~> r ~> check {
////        import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
////        import io.circe.generic.auto._
////        val res: UserLineListView = responseAs[Res].toEither.right.get
////        println( "response get user line list: " + res )
////        res.lists
////        ???
////      }
//
//      //      println( r )
////      entities shouldBe resBe
////      entities should not be resNotBe
//
//      s.shutdownActorSystem()
//
//
//    }
//  }
//
//}
//
//class GetUserLineListRoute_TestClass extends RoutesTestBase with GetUserLineListRouteTest{
//
//  override def server(initState: ApplicationState ): HttpServer_For_ImageMemory_App =
//    TestServerFactory.getTestServer( initState )
//}
