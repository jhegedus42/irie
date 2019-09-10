package app.server.httpServer.routes.post

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import app.server.utils.{GetTimeOnJVM, PrettyPrint}
import app.shared.comm.{PostRequest, RouteName}

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

private[routes] object PostRouteFactory {

  def getPostRoute[Req <: PostRequest](
  )(
    implicit
    classTag:  ClassTag[Req],
    classTag2: ClassTag[Req#PayLoad],
    logic:     RouteLogic[Req],
    dpl:       Decoder[Req#PayLoad],
    decoder:   Decoder[Req#Par],
    encoder:   Encoder[Req#Res],
    e:         ExecutionContext
  ): PostRoute[Req] = {

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

            println(s"""
                       |
                       |--------------------------------------------------
                       | Route
                       | $rn
                       | was called
                       | with :
                       | $s
                       |--------------------------------------------------
                       |
               """.stripMargin)

            val params: Req#Par =
              encdec
                .decodeParameters(ParametersAsJSON(s))
                .toOption
                .get

            val par_debug = params

            println(s"debug A310F8F2 - $par_debug")

//            implicit val ec = encdec

            val l: RouteLogic[Req] = logic

            val name = l.getRouteName

            println(s"debug 76FB201E : $name")

            val res: Future[Option[Req#Res]] =
              logic.getHttpReqResult(params)

            val res2: Future[String] =
              res.map((o: Option[Req#Res]) => {
                val encoded: ResultOptionAsJSON =
                  encdec.encodeResult(o)
                val optionAsString: String =
                  encoded.resultOptionAsJSON
                println(
                  s"debug : 0547B2B6 - we return :\n $optionAsString"
                )
                optionAsString
              })

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

    PostRoute[Req](res)
  }

}
