package app.client.ui.caching.viewCache

import app.shared.comm.views.{PostRequest, ViewHttpRouteName }
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private[caching] object REST_ForView {

  case class View_AJAX_Request_Params[V <: PostRequest](par: V#Par )

  case class View_AJAX_Result_JSON_Decoded_Successfully[V <: PostRequest](
      par: V#Par,
      res: V#Res)

  private[viewCache] def getViewFromServer[V <: PostRequest](
      requestParams: View_AJAX_Request_Params[V]
    )(implicit ct:   ClassTag[V],
      encoder:       Encoder[V#Par],
      decoder:       Decoder[V#Res]
    ): Future[View_AJAX_Result_JSON_Decoded_Successfully[V]] = {

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val routeName: ViewHttpRouteName =
      ViewHttpRouteName.getViewHttpRouteName[V]

    val url: String = routeName.name

    import io.circe.parser.decode
    import io.circe.syntax._

    val plain_params: V#Par = requestParams.par

    val json_line: String = plain_params.asJson.spaces2 // encode

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )

    val res1: Future[V#Res] =
      Ajax
        .post( url, json_line, headers = headers )
        .map( _.responseText )
        .map( (x: String) => {
          decode[V#Res]( x )
        } )
        .map( x => x.right.get )

    val res2: Future[View_AJAX_Result_JSON_Decoded_Successfully[V]] =
      res1.map(
        View_AJAX_Result_JSON_Decoded_Successfully( plain_params, _ )
      )
    res2
  }

}
