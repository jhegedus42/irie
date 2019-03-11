//package app.server.RESTService.routes.generalCRUD
//
//import app.server.RESTService.HttpServer_For_ImageMemory_App
//import app.server.RESTService.routes.RoutesTestBase
//import app.shared.data.ref.RefVal
//import app.shared.data.model.{DataType, LineText}
//import app.testHelpersServer.state.TestData
//import app.testHelpersShared.data.TestEntities
//
//import scala.collection.immutable.Seq
//
///**
//  * Created by joco on 14/12/2017.
//  */
//trait GetAllEntityRouteTest {
//  this: RoutesTestBase =>
//
//  "getAllEntity" should {
//    "return all entity" in {
//      import io.circe.generic.auto._
//
//      val s:        HttpServer_For_ImageMemory_App           = server(TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else)
//      val entities: Seq[RefVal[LineText]] = getAllEntitiesHelper( s, DataType.make[LineText])
//
//      val res:  Seq[RefVal[LineText]] = List( TestEntities.refValOfLineV0 )
//      val res2: Seq[RefVal[LineText]] = List( TestEntities.refValOfLineV0, TestEntities.refValOfLineV0 )
//
//      // assert that we got all lines
//      // 5587d5c97cc1457d8b629962b5ed30c4
//
//      //      println( r )
//      entities shouldBe res
//      entities should not be res2
//
//      s.shutdownActorSystem()
//    }
//  }
//
//}
