package app.client.ui.caching.entityCache

import app.client.ui.caching.CacheInterface
import app.client.ui.caching.REST.getEntity
import app.client.ui.caching.entityCache.EntityCacheStates.{EntityCacheState, Loaded, Loading}
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{RefVal, TypedRef}

import scala.util.Try
//import app.client.ui.components.cache.hidden._temp_disabled_ajax.AJAXGetEntityApi.InFlight_ReadEntity
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag



/**
  * This is a map that contains cached Entities.
  *
  * @tparam E
  */
private[caching] class EntityCache[E <: Entity](cacheInterface: CacheInterface ) {
  println( s"Constructor of EntityCacheMap" )

  val cacheMap = new MapForEntityCache[E]

  var nrOfAjaxReqSent = 0
  var nrOfAjaxReqReturnedAndHandled = 0

  private[caching] def readEntity(
      refToEntity: TypedRef[E]
    )(
      implicit
      decoder: Decoder[RefVal[E]],
      ct:      ClassTag[E]
    ): EntityCacheState[E] = {

    println( s"par: $refToEntity" )

    val res =
      cacheMap.getEntityOrExecuteAction( refToEntity ) {
        launchReadAjax( refToEntity )
      }

    res

  }

  private def launchReadAjax(
      ref: TypedRef[E]
    )(
      implicit decoder: Decoder[RefVal[E]],
      ct:               ClassTag[E]
    ): Unit = {

    nrOfAjaxReqSent = nrOfAjaxReqSent + 1

    println(
      s"LOGGER launchReadAjax, nrOfAjaxReqSent= $nrOfAjaxReqSent, " +
        s"nrOfAjaxReqReturnedAndHandled= $nrOfAjaxReqReturnedAndHandled"
    )

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val ajaxCallAsFuture: Future[RefVal[E]] = {
      val res: Future[RefVal[E]] = getEntity[E]( ref )
      res
    }

    ajaxCallAsFuture.onComplete(
      r => ajaxReqReturnHandler( r )
    )
  }

  private def ajaxReqReturnHandler(tryRefVal: Try[RefVal[E]] ): Unit = {
    println(
      s"ajaxReqReturnHandler called with parameter $tryRefVal"
    )

    tryRefVal.foreach(
      rv => cacheMap.insertIntoCacheAsLoaded( rv )
    )

    println(
      s"isAjaxReqStillPending=${cacheMap.isAjaxReqStillPending}, at ajaxReqReturnHandler"
    )

    if (!cacheMap.isAjaxReqStillPending) { //we trigger a re-render if this is the "last ajax request that came back"
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
