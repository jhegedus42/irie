package app.server.httpServer.routes.post

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import app.server.utils.{GetTimeOnJVM, PrettyPrint}
import app.shared.comm.{PostRouteType, RouteName}

import scala.concurrent.{ExecutionContext, Future}
import io.circe.{Decoder, Encoder, Json}

import scala.reflect.ClassTag
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.shared.comm.postRequests.marshall.{
  EncodersDecoders,
  ParametersAsJSON,
  ResultOptionAsJSON
}

//import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.Decoder.Result

private[routes] object PostRouteForAkkaHttpFactory {

  def getPostRoute[Req <: PostRouteType](
      )(
                                          implicit
                                          classTag:  ClassTag[Req],
                                          classTag2: ClassTag[Req#PayLoad],
                                          logic:     RouteLogic[Req],
                                          dpl:       Decoder[Req#PayLoad],
                                          decoder:   Decoder[Req#Par],
                                          encoder:   Encoder[Req#Res],
                                          e:         ExecutionContext
  ): PostRouteForAkkaHttp[Req] = {

    val res: Route =
      post {
        val rn = RouteName.getRouteName[Req].name
        path(rn) {
          entity(as[String]) { s: String =>
            val encdec = EncodersDecoders

            import io.circe.parser._
            import io.circe.{Decoder, Error, _}

            import io.circe.parser._
            import io.circe.{Decoder, Encoder, Error, _}

            val params: Req#Par =
              encdec.decodeParameters(ParametersAsJSON(s)).toOption.get

            implicit val ec = encdec

            val res: Future[Option[Req#Res]] = logic.getHttpReqResult(params)

            val res2: Future[String] =
              res.map(encdec.encodeResult(_).resultOptionAsJSON)

            complete(res2)
          }
        }
      }

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

    PostRouteForAkkaHttp[Req](res)
  }

}
