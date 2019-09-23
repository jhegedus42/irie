package app.client.ui.caching.cache.comm

import app.shared.comm.{PostRequest, PostRequestType, RouteName}
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private[caching] object AJAXCalls {

  case class AjaxCallPar[
    RT  <: PostRequestType,
    Req <: PostRequest[RT]
  ](par: Req#ParT)

  case class PostAJAXRequestSuccessfulResponse[
    RT  <: PostRequestType,
    Req <: PostRequest[RT]
  ](par: Req#ParT,
    res: Req#ResT)

  private[cache] def sendPostAjaxRequest[
    RT  <: PostRequestType,
    Req <: PostRequest[RT]
  ](requestParams: AjaxCallPar[RT, Req]
  )(
    implicit
    ct:                       ClassTag[Req],
    classTag2:                ClassTag[Req#PayLoadT],
    encoder:                  Encoder[Req#ParT],
    decoder:                  Decoder[Req#ResT],
    executionContextExecutor: ExecutionContextExecutor
  ): Future[PostAJAXRequestSuccessfulResponse[RT,Req]] = {

//    implicit def executionContext: ExecutionContextExecutor =
//      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val routeName: RouteName =
      RouteName.getRouteName[RT,Req]

    val url: String = routeName.name

    import io.circe.parser.decode
    import io.circe.syntax._

    val plain_params: Req#ParT = requestParams.par

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

    val res1: Future[Req#ResT] =
      Ajax
        .post(url, json_line, headers = headers)
        .map(_.responseText)
        .map((x: String) => {
          decode[Req#ResT](x)
        })
        .map(x => x.right.get)

    val res2: Future[PostAJAXRequestSuccessfulResponse[RT,Req]] =
      res1.map(
        PostAJAXRequestSuccessfulResponse(plain_params, _)
      )
    res2
  }

}
