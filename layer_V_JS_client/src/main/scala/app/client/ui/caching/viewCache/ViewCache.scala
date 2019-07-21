package app.client.ui.caching.viewCache

import app.client.ui.caching.CacheInterface
import app.client.ui.caching.REST.getEntity
import app.client.ui.caching.entityCache.EntityCacheStates.EntityCacheState
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{ViewHttpRouteName, ViewHttpRouteNameProvider}
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{RefVal, TypedRef}
import io.circe.Encoder
import org.scalajs.dom.ext.Ajax

import scala.util.Try
//import app.client.ui.components.cache.hidden._temp_disabled_ajax.AJAXGetEntityApi.InFlight_ReadEntity
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag


/**
  * This is a map that contains cached Entities.
  *
  * @tparam V
  */
private[caching] class ViewCache[V <: View](cacheInterface: CacheInterface ) {
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  println( s"Constructor of EntityCacheMap" )

  val cacheMap = new MapForViewCache[V]

  var nrOfAjaxReqSent = 0
  var nrOfAjaxReqReturnedAndHandled = 0


  private def launchAjaxReqToGetViewResult(
      ref: V#Par
    )(
      implicit decoder: Decoder[V]],
      ct:               ClassTag[V]
    ): Unit = {

    nrOfAjaxReqSent = nrOfAjaxReqSent + 1

    println(
      s"LOGGER launchReadAjax, nrOfAjaxReqSent= $nrOfAjaxReqSent, " +
        s"nrOfAjaxReqReturnedAndHandled= $nrOfAjaxReqReturnedAndHandled"
    )

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val ajaxCallAsFuture: Future[RefVal[V]] = {
      val res: Future[RefVal[V]] = getEntity[V]( ref )
      res
    }

    ajaxCallAsFuture.onComplete(
      r => ajaxReqReturnHandler( r )
    )
  }



  private def ajaxReqReturnHandler(tryRefVal: Try[RefVal[V]] ): Unit = {
    println(
      s"ajaxReqReturnHandler called with parameter $tryRefVal"
    )

    tryRefVal.foreach(
      rv => cacheMap.insertIntoCacheAsLoaded( rv )
    )

    println(
      s"isAjaxReqStillPending=${cacheMap.isThereStillAjaxRequestsWhichHasNotReturnedYet}, at ajaxReqReturnHandler"
    )

    if (!cacheMap.isThereStillAjaxRequestsWhichHasNotReturnedYet) { //we trigger a re-render if this is the "last ajax request that came back"
      println( s"LAST AJAX call returned => re-render needs to be triggered" )
      cacheInterface.reRenderShouldBeTriggered()
    }

    nrOfAjaxReqReturnedAndHandled = nrOfAjaxReqReturnedAndHandled + 1

    println(
      s"LOGGER ajaxReqReturnHandler, nrOfAjaxReqSent= $nrOfAjaxReqSent, " +
        s"nrOfAjaxReqReturnedAndHandled= $nrOfAjaxReqReturnedAndHandled"
    )
  }

}
