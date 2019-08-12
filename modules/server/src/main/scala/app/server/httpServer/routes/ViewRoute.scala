package app.server.httpServer.routes

import akka.http.scaladsl.server.directives.RouteDirectives.complete
import app.server.logic.ServerSideLogic.ServerLogicTypeClass
import app.shared.comm.views.View
//import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.ViewHttpRouteNameProvider
import app.shared.comm.views.ViewHttpRouteName
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag
//  import akka.http.scaladsl.server.directives.MethodDirectives.get
//  import akka.http.scaladsl.server.directives.ParameterDirectives.parameters
//  import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
//import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//import io.circe.generic.auto._ // this cannot be removed, or can it ? maybeTODO-later

object ViewRoute {
  def getRouteForView[V <: View](
    )(
      implicit
      classTag:    ClassTag[V],
      serverLogic: ServerLogicTypeClass[V],
      decoder:     Decoder[V#Par],
      encoder:     Encoder[V#Res]
    ): Route = {

    val routeName: ViewHttpRouteName = ViewHttpRouteName.getViewHttpRouteName[V]

    val res: Route = {

        post {
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

    res
  }

}