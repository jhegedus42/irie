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
      logic:     RouteLogicTypeClass[Req],
      dpl:       Decoder[Req#PayLoad],
      decoder:   Decoder[Req#Par],
      encoder:   Encoder[Req#Res],
      e:         ExecutionContext
  ): PostRouteForAkkaHttp[Req] = {

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
//          entity(as[Req#Par]) { params: Req#Par =>
          entity(as[String]) { s: String =>
            val encdec= EncodersDecoders
//               val res: Result[Req#Par] =decoder.decodeJson(Json(s))
//               val res2: Option[Req#Par] =res.toOption

            import io.circe.parser._
            import io.circe.{Decoder, Error, _}

            val params: Req#Par = //todo fix this unsafeness
              encdec.decodeParameters(ParametersAsJSON(s)).toOption.get

//               val params: Req#Par = res2.get //todo fix this unsafeness

            // WE NEED TO REWRITE THIS TO JSON => todo-continue-here

            // todo-one-day - get rid of entity marshalling magic
            //  https://dynalist.io/d/Qw8GJh1kUfs_QDhSIs_gixtA#z=dnrnt_Qf6boIBkkJXYdjHTmI

            implicit val ec=encdec

            val res: Future[Option[Req#Res]] = logic.getResult(params)

            val res2: Future[String] = res.map(encdec.encodeResult(_).string)
            val res3 = res2.map(HttpResponse().withEntity(_))
//            val resp=HttpResponse().withEntity()
            complete(res2)
          }
        }
      }

    PostRouteForAkkaHttp[Req](res)
  }

}
