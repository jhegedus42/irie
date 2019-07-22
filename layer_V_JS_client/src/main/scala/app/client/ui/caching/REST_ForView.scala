package app.client.ui.caching

import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{ViewHttpRouteName, ViewHttpRouteNameProvider}
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private[caching] object REST_ForView {

  case class View_AJAX_Request_Params[V <: View](par: V#Par )

  case class View_AJAX_Result[V <: View](par: V#Par, res: V#Res )

  private[viewCache] def getView[V <: View](
      requestParams: View_AJAX_Request_Params[V]
    )(implicit ct:   ClassTag[V],
      encoder:       Encoder[V#Par],
      decoder:       Decoder[V#Res]
    ): Future[View_AJAX_Result[V]] = {

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val routeName: ViewHttpRouteName =
      ViewHttpRouteNameProvider.getViewHttpRouteName[V]()

    val url: String = routeName.name

    import io.circe.parser.decode
    import io.circe.syntax._

    val plain_params: V#Par =requestParams.par

    val json_line: String = plain_params.asJson.spaces2 // encode

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )

    val res1: Future[V#Res] =
      Ajax
        .post( url, json_line, headers = headers )
        .map( _.responseText )
        .map( (x: String) => { decode[V#Res]( x ) } )
        .map( x => x.right.get )

    val res2 = res1.map( View_AJAX_Result(plain_params,_) )

    res2
  }

}
