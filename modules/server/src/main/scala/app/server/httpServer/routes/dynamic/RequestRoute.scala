package app.server.httpServer.routes.dynamic

import akka.http.scaladsl.server.directives.RouteDirectives.complete
import app.server.httpServer.routes.dynamic.logic.ServerSideLogic.ServerLogicTypeClass
import app.shared.comm.{PostRequest, RouteName}

import scala.concurrent.Future
//import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.ViewHttpRouteNameProvider
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

private[routes] case class RequestRoute[Req <: PostRequest](route: Route )

private[routes] object RequestRoute {

  def getRoute[Req <: PostRequest]( )(
      implicit
      classTag:    ClassTag[Req],
      classTag2:    ClassTag[Req#PayLoad],
      serverLogic: ServerLogicTypeClass[Req],
      decoder:     Decoder[Req#Par],
      encoder:     Encoder[Req#Res]
  ): RequestRoute[Req] = {

    val routeName: RouteName = RouteName.getRouteName[Req]
    val pathName: String = routeName.name

    println(s"We set up a route with the path of :\n$pathName")

    val res: Route = {

      post {
        path( pathName ) {

          entity( as[Req#Par] ) { params: Req#Par =>
            val res: Future[Option[Req#Res]] =
              serverLogic.getResult( params )

            println(
              s"""
                |
                | ServerLogic was called with parameters:
                | $params
                |
               """.stripMargin)


            complete( res )
          }

        }
      }
    }

    RequestRoute[Req]( res )
  }

}
