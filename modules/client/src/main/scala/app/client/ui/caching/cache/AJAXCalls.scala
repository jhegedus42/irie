package app.client.ui.caching.cache

import app.shared.comm.{Request, RouteName}
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private[caching] object AJAXCalls {

  case class AjaxCallParams[Req <: Request](par: Req#Par )

  case class DecondingSuccess[Req <: Request](
      par: Req#Par,
      res: Req#Res)

  private[cache] def getResults[Req <: Request](
      requestParams: AjaxCallParams[Req]
    )(implicit ct:   ClassTag[Req],
      encoder:       Encoder[Req#Par],
      decoder:       Decoder[Req#Res]
    ): Future[DecondingSuccess[Req]] = {

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

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

    val res1: Future[Req#Res] =
      Ajax
        .post( url, json_line, headers = headers )
        .map( _.responseText )
        .map( (x: String) => {
          decode[Req#Res]( x )
        } )
        .map( x => x.right.get )

    val res2: Future[DecondingSuccess[Req]] =
      res1.map(
        DecondingSuccess( plain_params, _ )
      )
    res2
  }

}
