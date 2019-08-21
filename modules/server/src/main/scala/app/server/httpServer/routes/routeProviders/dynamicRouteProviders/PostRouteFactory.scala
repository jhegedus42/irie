package app.server.httpServer.routes.routeProviders.dynamicRouteProviders

import akka.http.scaladsl.server.directives.RouteDirectives.complete
import app.server.httpServer.routes.routeProviders.dynamicRouteProviders.serverLogicAsTypeClasses.ServerLogicTypeClass
import app.server.utils.GetTimeOnJVM
import app.shared.comm.{PostRequest, RouteName}

import scala.concurrent.Future
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._



private[routes] object PostRouteFactory {

  def createPostRoute[Req <: PostRequest]( )(
      implicit
      classTag:    ClassTag[Req],
      classTag2:    ClassTag[Req#PayLoad],
      serverLogic: ServerLogicTypeClass[Req],
      dpl:Decoder[Req#PayLoad],
      decoder:     Decoder[Req#Par],
      encoder:     Encoder[Req#Res]
  ): PostRoute[Req] = {

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
                | vvvvvvvvvvv------------------------------
                |
                |
                | ServerLogic was called with parameters:
                | $params
                |
                | Time is :
                | ${GetTimeOnJVM.time.toString}
                |
                |
                | ^^^^^^^^^^^------------------------------
                |
               """.stripMargin)


            complete( res )
          }

        }
      }
    }

    PostRoute[Req]( res )
  }

}
