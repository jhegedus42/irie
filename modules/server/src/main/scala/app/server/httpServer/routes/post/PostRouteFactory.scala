package app.server.httpServer.routes.post

import akka.http.scaladsl.server.directives.RouteDirectives.complete
import app.server.utils.{GetTimeOnJVM, PrettyPrint}
import app.shared.comm.{PostRequest, RouteName}

import scala.concurrent.Future
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

private[routes] object PostRouteFactory {

  def getPostRoute[Req <: PostRequest](
      )(
                                        implicit
                                        classTag:  ClassTag[Req],
                                        classTag2: ClassTag[Req#PayLoad],
                                        logic:     RouteLogic[Req],
                                        dpl:       Decoder[Req#PayLoad],
                                        decoder:   Decoder[Req#Par],
                                        encoder:   Encoder[Req#Res]
  ): PostRoute[Req] = {

    def log(params: Req#Par): Unit = {
      println(s"""
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
                 |
                 | ${RouteName.getRouteName[Req].name}
                 |
                 | ^^^^^^^^^^^------------------------------
                 |
               """.stripMargin)
    }

    val res: Route =
      post {
        path(RouteName.getRouteName[Req].name) {
          entity(as[Req#Par]) { params: Req#Par =>
            // todo-one-day - get rid of entity marshalling magic
            //  https://dynalist.io/d/Qw8GJh1kUfs_QDhSIs_gixtA#z=dnrnt_Qf6boIBkkJXYdjHTmI
            complete(logic.getResult(params))
          }
        }
      }

    PostRoute[Req](res)
  }

}
