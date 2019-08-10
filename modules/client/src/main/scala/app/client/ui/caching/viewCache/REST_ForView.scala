package app.client.ui.caching.viewCache

import app.shared.comm.views.{View, ViewHttpRouteName, ViewHttpRouteNameProvider}
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private[caching] object REST_ForView {

  case class View_AJAX_Request_Params[V <: View](par: V#Par )

  case class View_AJAX_Result_JSON_Decoded_Successfully[V <: View](
      par: V#Par,
      res: V#Res)

  private[viewCache] def getViewFromServer[V <: View](
      requestParams: View_AJAX_Request_Params[V]
    )(implicit ct:   ClassTag[V],
      encoder:       Encoder[V#Par],
      decoder:       Decoder[V#Res]
    ): Future[View_AJAX_Result_JSON_Decoded_Successfully[V]] = {

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val routeName: ViewHttpRouteName =
      ViewHttpRouteNameProvider.getViewHttpRouteName[V]

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
        // TODO-one-day :
        //  OFFLINE => CATCH the offline error here ^^^
        //  we get an exception and the app "freezes" if the network is offline
        //  and we try to call this method
        //  things blow up at this post call, in that situation
        //  this is more important than the "decoding issue below"
        //  but maybe the two are related... need to look into this and
        //  debug a bit ... what happens with this AJAX POST call, when
        //  the network is offline
        //  but for now, we are only gonna deal with the happy path ...
        //  since we are writing a "toy" app for "prototype demo purposes"
        //  it can freeze and can be buggy, but it should "work" i.e. be usable
        //  and "ready" :), ready but buggy is better than not ready but perfect :)
        .map( _.responseText )
        .map( (x: String) => {
          decode[V#Res]( x )
        } )
        // TODO-one-day : handle the decoding error here,
        //  more gracefully, this is here very ugly below
        //  sorry about this...
        //  maybe along the Future / Try / onComplete ()
        //  ... etc ... line, one could handle this situation
        //  or ... there are lot of options ...
        //  using the current result type of `getView(...)`
        .map( x => x.right.get )

    val res2: Future[View_AJAX_Result_JSON_Decoded_Successfully[V]] =
      res1.map(
        View_AJAX_Result_JSON_Decoded_Successfully( plain_params, _ )
      )

    res2
  }

}
