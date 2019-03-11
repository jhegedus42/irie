//package app.server.RESTService.routes
//
//import akka.http.scaladsl.model.HttpRequest
//import akka.http.scaladsl.server.Route
//import app.server.RESTService.HttpServer_For_ImageMemory_App
//import app.testHelpersShared.data.TestDataLabels.TestDataLabel
//
//trait ResetStateRoute_Test {
//  this: RoutesTestBase =>
//
//  def resetState(s: HttpServer_For_ImageMemory_App, tdl: TestDataLabel ): Unit = {
//    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//    import io.circe.generic.auto._
//    // ^^^ ez kell ide hogy a valaszt tudjuk dekodolni
//
//    val r:   Route       = s.route
//    val req: HttpRequest = Post( "/resetState", tdl )
//
//    println( "Put request:" + req )
//
//    val r2: String = req ~> r ~> check {
//
//
//      val result: String = responseAs[String]
//
//      println( "response from Post request:" + result )
//
//      assert( result === "resetted" )
//      result
//    }
//
//  }
//
//
//    "reset route" should {
//      "set the db state" in {
//        // to do later ... maybe ...
//      }
//
//    }
//
//}
