package app.server.httpServer.routes.routeProviders.dynamicRouteProviders

import akka.http.scaladsl.server.directives.RouteDirectives.complete
import app.server.httpServer.routes.logic.serverLogicAsTypeClasses.ServerLogicTypeClass
import app.server.utils.{GetTimeOnJVM, PrettyPrint}
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
            // todo-one-day
            //  get rid of this magical entity marshalling
            //  and do the marshalling by hand using circe's
            //  Decoder[Req#Par] directly from json ...
            //  this way we can debug the requests ... at all
            //  and print out to the console the incoming JSON's.
            //  Now, they get "lost somewhere in the translation".
            //  => let's try to extract the json in the ping_pong
            //     route
            //  for example, let's look at :
            //  https://doc.akka.io/docs/akka-http/current/routing-dsl/directives/index.html#basics
            //
            //




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
                |
                | or the same, just pretty printed:
                |
                | ${PrettyPrint.prettyPrint(params)}
                |
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
