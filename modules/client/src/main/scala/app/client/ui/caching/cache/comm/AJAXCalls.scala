package app.client.ui.caching.cache.comm

import app.shared.comm.{PostRequest, RouteName}
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private[caching] object AJAXCalls {

  case class AjaxCallPar[Req <: PostRequest](par: Req#Par)

  case class PostAJAXRequestSuccessfulResponse[Req <: PostRequest](
    par: Req#Par,
    res: Req#Res
  )

  private[cache] def sendPostAjaxRequest[Req <: PostRequest](
    requestParams: AjaxCallPar[Req]
  )(
    implicit
    ct:        ClassTag[Req],
    classTag2: ClassTag[Req#PayLoad],
    encoder:   Encoder[Req#Par],
    decoder:   Decoder[Req#Res],
    executionContextExecutor: ExecutionContextExecutor

  ): Future[PostAJAXRequestSuccessfulResponse[Req]] = {

//    implicit def executionContext: ExecutionContextExecutor =
//      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val routeName: RouteName =
      RouteName.getRouteName[Req]

    val url: String = routeName.name

    import io.circe.parser.decode
    import io.circe.syntax._

    val plain_params: Req#Par = requestParams.par

    val json_line: String = plain_params.asJson.spaces2 // encode

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )

    println(s"""
               |
               |
               |vvvvvvvvvvvvvvvvvvvvvvvvvvvv
               |
               |In `private[caching] object AJAXCalls `,
               |
               |in `val json_line: String = plain_params.asJson.spaces2`
               |
               |`json_line` is :
               |
               |
               |$json_line
               |
               |
               |In `val url: String = routeName.name`
               |
               |`url` is:
               |
               |$url
               |
               |^^^^^^^^^^^^^^^^^^^^^^^^^^^^
               |
               |
       """.stripMargin)

    val res1: Future[Req#Res] =
      Ajax
        .post(url, json_line, headers = headers)
        .map(_.responseText)
        .map((x: String) => {
          decode[Req#Res](x)
        })
        .map(x => x.right.get)

    val res2: Future[PostAJAXRequestSuccessfulResponse[Req]] =
      res1.map(
        PostAJAXRequestSuccessfulResponse(plain_params, _)
      )
    res2
  }

}
