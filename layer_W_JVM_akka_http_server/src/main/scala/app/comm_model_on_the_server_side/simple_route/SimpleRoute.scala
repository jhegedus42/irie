package app.comm_model_on_the_server_side.simple_route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
//  import akka.http.scaladsl.server.directives.MethodDirectives.get
//  import akka.http.scaladsl.server.directives.ParameterDirectives.parameters
//  import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

// Random UUID: a1704dea5c4b41bf8297f4d7a9f3c3af
// commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
// Date: Tue Aug 14 08:01:10 EEST 2018
case class PairOfInts(x:  Int, y: Int )


case class SumOfInts(sum: Int )

// Random UUID: b503c267936147f0a52b2d47dddf93c9
// commit fc5bb550a0436ada8876f7c8a18d4b4bf9407091
// Date: Sun Jul 29 16:37:25 CEST 2018

object SumIntViewRoute_For_Testing {

  // Random UUID: 19590923ebc1461d9960139f314c154a
  // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
  // Date: Sun Sep  2 18:29:09 EEST 2018

  def route: Route = {
    cors() {
      post {
        path( "getSumOfIntsView" ) {
          entity( as[PairOfInts] ) {
            params: PairOfInts =>
              val res: SumOfInts = SumOfInts( params.x + params.y )
              complete( res )
          }
        }
      }
    }
  }

}
