package app.client.ui.caching.viewCache

import app.client.ui.caching.CacheInterface
import app.client.ui.caching.viewCache.ViewCacheStates.{
  ViewCacheState,
  ViewLoaded,
  ViewLoading
}
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.shared.utils.logging.LoggingHelperFunctions
import REST_ForView.{
  View_AJAX_Request_Params,
  View_AJAX_Result_JSON_Decoded_Successfully,
  getView
}
import app.client.ui.caching.CacheInterface
import app.client.ui.caching.viewCache.ViewCacheStates.ViewCacheState
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
import scala.util.Try

import scala.util.Try

/**
  * @param cacheInterface This is a dependency.
  * @tparam V
  */
private[caching] class ViewCache[V <: View](cacheInterface: CacheInterface ) {

  private[this] var map: Map[V#Par, ViewCacheState[V]] = Map()

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private[this] def launchAjaxReqToGetViewResult(
      par: V#Par
    )(
      implicit
      decoder: Decoder[V#Res],
      encoder: Encoder[V#Par],
      ct:      ClassTag[V]
    ): Unit = {

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val req = View_AJAX_Request_Params( par )

    val ajaxCallAsFuture
      : Future[View_AJAX_Result_JSON_Decoded_Successfully[V]] =
      getView[V]( req )

    ajaxCallAsFuture.onComplete(
      (r: Try[View_AJAX_Result_JSON_Decoded_Successfully[V]]) =>
        ajaxReqReturnHandler( r )
    )
  }

  private[this] def ajaxReqReturnHandler(
      tryRes: Try[REST_ForView.View_AJAX_Result_JSON_Decoded_Successfully[V]]
    ): Unit = {

    tryRes.foreach(
      (rv: REST_ForView.View_AJAX_Result_JSON_Decoded_Successfully[V]) =>
        insertIntoCacheAsLoaded( rv.par, rv.res )
    )

    if (!isThereStillAjaxRequestsWhichHasNotReturnedYet)
      cacheInterface.reRenderShouldBeTriggered()
  }

  private[this] def isThereStillAjaxRequestsWhichHasNotReturnedYet: Boolean = {
    val res = map.valuesIterator.exists(
      (x: ViewCacheState[V]) => x.isLoading
    )
    res
  }

  private[this] def insertIntoCacheAsLoaded(
      par: V#Par,
      res: V#Res
    ): ViewLoaded[V] = {

    val toBeInsertedIntoCache: ViewLoaded[V] = ViewLoaded( par, res )

    val cacheBeforeInsertion = map

    val cacheAfterInsertion = cacheBeforeInsertion + (par -> toBeInsertedIntoCache)

    this.map = cacheAfterInsertion

    LoggingHelperFunctions.logMethodCall(
      "insertIntoCacheAsLoaded(par: V#Par, res: V#Res )",
      s"""
         |
        |
        | we inserted $toBeInsertedIntoCache into the cache, which had
         |
        | the following content before insertion :
         | ${prettyPrint( cacheBeforeInsertion )}
         |
        |
        | and it has following content after insertion :
         | ${prettyPrint( cacheAfterInsertion )}
         |
        |
      """.stripMargin
    )

    toBeInsertedIntoCache
  }

  def prettyPrint(aMap: Map[_, _] ): String = aMap.foldLeft( "" ) {
    ( key, value ) =>
      {
        s"""
         | >
         | >
         | >vvvvvvvvvvvvvvvvvvvvvvvvvvvv
         | >
         | > ENTRY BEGINS
         | >
         | > Key:
         | >
         |$key
         | >
         | > Value:
         | >
         |$value
         | >
         | >
         | > ENTRY ENDS
         | >
         | >
         | >^^^^^^^^^^^^^^^^^^^^^^^^^^^
         | >
         | >
       """.stripMargin
      }
  }

  private[viewCache] def getViewReqResultOrExecuteAction(
      par:    V#Par
    )(action: => Unit
    ): ViewCacheState[V] = {

    val res: ViewCacheState[V] =
      if (!map.contains( par )) {
        val loading = ViewLoading( par )
        println(
          s"""
             |----------vvvvvvvvvvvvvvvvvvv------------------
             | getViewReqResultOrExecuteAction is called
             |
          | map content is :
             |
          | $map
             |
          | the state is "ViewLoading" :
             |
          | $loading
             | ----------^^^^^^^^^^^^^^^^^^------------------
        """.stripMargin
        )
        insertIntoCacheAsLoading( par )
        action
        loading
      } else map( par )
    res

  }

  private[this] def insertIntoCacheAsLoading(par: V#Par ): ViewLoading[V] = {

    println(
      s"""
         |
         |----------vvvvvvvvvvvvvvvvvvv------------------
         |
         | insertIntoCacheAsLoading( .. ) is called :
         |
         | we insert $par into the cache as loading"
         |
         | ----------^^^^^^^^^^^^^^^^^^------------------
         |
       """.stripMargin
    )

    val vl = ViewLoading( par )

    val newMap = map + (par -> vl)
    this.map = newMap

    vl

  }

}
