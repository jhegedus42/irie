//package app.server.RESTService.routes
//
//import akka.http.scaladsl.server.Route
//import akka.http.scaladsl.testkit.ScalatestRouteTest
//import app.server.RESTService.HttpServer_For_ImageMemory_App
//import app.server.RESTService.mocks.TestServerFactory
//import app.server.RESTService.routes.generalCRUD.{GetAllEntityRouteTest, GetEntityRouteTest}
//import app.server.persistence.ApplicationState
//import app.shared.data.model.Entity.Entity
//import app.shared.data.ref.RefVal
//import app.shared.data.model.{DataType, LineText}
//import app.shared.rest.routes.crudRequests.{GetAllEntitiesRequest, GetEntityRequest}
//import app.testHelpersServer.state.TestData
//import io.circe.Decoder
//import org.scalatest.{Assertion, Matchers, WordSpec}
//
//import scala.collection.immutable.Seq
//import scala.reflect.ClassTag
//
///**
//  * - Mi az istenre használjuk ezt trait-et ?
//  * - Hát valami olyasmire, hogy vannak benne több teszt által használható metódusok is
//  *   amiket kár lenne minden egyes tesztbe külön beletenni. Ezért inkább a tesztek ezt
//  *   a trait-et keverik be maguknak (self type annotation-nal deklarálva).
//  *
//  * these are things that are used by all tests that test the routes
//  * making things more dry
//  * this is a common denominator for all route tests
//  */
//trait RoutesTestBase extends WordSpec with Matchers with ScalatestRouteTest {
//
//  /**
//    *
//    * - Vki vszeg felülírja, de minek ?
//    *
//    * És miért van benne ez a state cucc ?
//    * Eleve mi az a State ?
//    *
//    * @param initialApplicationState Ez megmondja, hogy mi legyen az app álapota,
//    *                                mikor elindul és még semmi semmi nem történt vele.
//    * @return
//    */
//  def server(initialApplicationState: ApplicationState ): HttpServer_For_ImageMemory_App
//
//  type ResInBase = GetEntityRequest[LineText]#Result
//
//  def testGetEntityHelper(url: String, assert: ResInBase => Unit ): Unit = {
//
//    val s: HttpServer_For_ImageMemory_App = server(TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else)
//    val r: Route     = s.route
//    Get( url ) ~> r ~> check {
//
//      import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//      import io.circe.generic.auto._
//      // ^^^ ez kell ide hogy a valaszt tudjuk dekodolni a kovetkezo sorban
//      val r: ResInBase = responseAs[ResInBase]
//      assert( r )
//      println( r )
//
//    }
//    s.shutdownActorSystem()
//  }
//
//  def assert_RefVal_for_LineText_is_present(
//                                             s:                                       HttpServer_For_ImageMemory_App,
//                                             refVal:                                  RefVal[LineText],
//                                             shouldWeTestForEqualityOrForNotEquality: Boolean
//    ): Assertion = {
//
//    val r: Route = s.route
//
//    val url: String = GetEntityRequest[LineText]().queryURL( refVal.r )
//
//    Get( url ) ~> r ~> check {
//
//      import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//      import io.circe.generic.auto._
//      // ^^^ ez kell ide hogy a valaszt tudjuk dekodolni a kovetkezo sorban
//
//      type Res = GetEntityRequest[LineText]#Result
//      val res: Res =
//        responseAs[Res] //dd25434d2997499aa3a984a2af991ffe
//      println( "---- assert LineText is present start ----" )
//      println( "response " + res )
//      println( "refVal : " + refVal )
//      println( "should be equal ? " + shouldWeTestForEqualityOrForNotEquality )
//      println( "---- assert LineText is present end ----" )
//      //                             1 shouldNot be 2
//      assert( !shouldWeTestForEqualityOrForNotEquality ^ (res.toEither.right.get == refVal) )
//    }
//  }
//
//  /**
//    * - Ez pl. mi a gecire jó ?
//    *
//    * - Ki a IandInak kell ez ?
//    *
//    * @param restService
//    * @param functionToRun
//    * @tparam T
//    * @return
//    */
//  def runWithServer[T](restService: HttpServer_For_ImageMemory_App )(functionToRun: HttpServer_For_ImageMemory_App => T ): T = {
//    val res: T = functionToRun( restService )
//    restService.system.terminate()
//    res
//  }
//
//  def getAllEntitiesHelper[E <: Entity: ClassTag: Decoder: GetAllEntitiesRequest](
//                                                                                   server:     HttpServer_For_ImageMemory_App,
//                                                                                   entityType: DataType
//    ): Seq[RefVal[E]] = {
//
//    val r: Route = server.route
//    val gAEs  = implicitly[GetAllEntitiesRequest[E]]
//    val gAEsL = GetAllEntitiesRequest[E]()
//    type Res = gAEsL.Result
//
//    val url: String = gAEs.queryURL
//
//    val res2 = Get( url ) ~> r ~> check {
//      import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//      import io.circe.generic.auto._
//      val res = responseAs[Res].toEither.right.get
//      println( "response getAllEntHelper: " + res )
//      res
//    }
//
//    println( "outer response " + res2 )
//    res2
//
//  }
//
//}
//
/////**
////  * This defines what tests to run.
////  * This is actually the "test class" which is looked at by the test runner.
////  */
////class RoutesTest_PersActor_Class extends RoutesTestBase with GetEntityRouteTest with GetAllEntityRouteTest {
////
////  override def server(initState: ApplicationState ): HttpServer_For_ImageMemory_App =
////    TestServerFactory.getTestServer( initState )
////}
