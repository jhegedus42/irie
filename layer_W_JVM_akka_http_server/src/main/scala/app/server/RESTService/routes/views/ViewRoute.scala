package app.server.RESTService.routes.views

import akka.http.scaladsl.server.Route
import app.comm_model_on_the_server_side.serverSide.akkaHttpWebServer.GetViewRequestHandler
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import app.comm_model_on_the_server_side.serverSide.logic.ServerSideLogic.ServerLogicTypeClass
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{
  ViewHttpRouteName,
  ViewHttpRouteNameProvider
}
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag
//  import akka.http.scaladsl.server.directives.MethodDirectives.get
//  import akka.http.scaladsl.server.directives.ParameterDirectives.parameters
//  import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

object ViewRoute {
  // Random UUID: 48f0b22025614b9693a1a6e78461037e
  // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
  // Date: Sun Sep  2 18:34:49 EEST 2018

  def getRouteForView[V <: View](
    )(
      implicit
      classTag:    ClassTag[V],
      serverLogic: ServerLogicTypeClass[V],
      decoder:     Decoder[V#Par],
      encoder:     Encoder[V#Res]
    ): Route = {

    val routeName: ViewHttpRouteName = ViewHttpRouteNameProvider.getViewHttpRouteName[V]()

    val res: Route = {
      cors() {
        post {
          // Random UUID: 6642f88b95c44dbea5dcb699d67aef83
          // path( "getSumOfIntsView" ) {
          val pathName: String = routeName.name
          path( pathName ) {

            entity( as[V#Par] ) {
              params: V#Par =>
                val res: Option[V#Res] = serverLogic.getView( params )
                complete( res )
            }

          }
        }
      }
    }

    res
  }

}
