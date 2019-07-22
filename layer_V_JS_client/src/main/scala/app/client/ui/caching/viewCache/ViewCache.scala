package app.client.ui.caching.viewCache

import REST_ForView.View_AJAX_Request_Params
import app.client.ui.caching.CacheInterface
import app.client.ui.caching.viewCache.ViewCacheStates.ViewCacheState
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
import scala.util.Try

private[caching] class ViewCache[V <: View](cacheInterface: CacheInterface ) {
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  println( s"Constructor of EntityCacheMap" )

  private [this] val cacheMap = new MapForViewCache[V]

  private [this] var nrOfAjaxReqSent = 0
  private [this] var nrOfAjaxReqReturnedAndHandled = 0

  private[this] def launchAjaxReqToGetViewResult(
      par:              V#Par
    )(implicit decoder: Decoder[V#Res],
      encoder:          Encoder[V#Par],
      ct:               ClassTag[V]
    ): Unit = {

    nrOfAjaxReqSent = nrOfAjaxReqSent + 1

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val req = View_AJAX_Request_Params( par )

    val ajaxCallAsFuture
      : Future[REST_ForView.View_AJAX_Result_JSON_Decoded_Successfully[V]] =
      REST_ForView.getView[V]( req )

    ajaxCallAsFuture.onComplete(
      (r: Try[REST_ForView.View_AJAX_Result_JSON_Decoded_Successfully[V]]) =>
        ajaxReqReturnHandler( r )
    )
  }

  private def ajaxReqReturnHandler(
      tryRes: Try[REST_ForView.View_AJAX_Result_JSON_Decoded_Successfully[V]]
    ): Unit = {

    tryRes.foreach(
      (rv: REST_ForView.View_AJAX_Result_JSON_Decoded_Successfully[V]) =>
        cacheMap.insertIntoCacheAsLoaded( rv.par,rv.res )
    )

    if (!cacheMap.isThereStillAjaxRequestsWhichHasNotReturnedYet)
      cacheInterface.reRenderShouldBeTriggered()

    nrOfAjaxReqReturnedAndHandled = nrOfAjaxReqReturnedAndHandled + 1

  }

  private[caching] def getViewCacheState(par:V#Par):ViewCacheState[V] = {
    ???
  }

}
