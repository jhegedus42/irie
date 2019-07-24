package app.client.ui.caching.viewCache

import app.client.ui.caching.CacheInterface
import app.client.ui.caching.viewCache.REST_ForView.{View_AJAX_Request_Params, getViewFromServer}
import app.client.ui.caching.viewCache.ViewCacheStates.{ViewCacheState, ViewLoaded, ViewLoading}
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

/**
  * @param cacheInterface This is a dependency.
  * @tparam V
  */
private[caching] class ViewCache[V <: View]( cacheInterface: CacheInterface ) {

  implicit def executionContext: ExecutionContextExecutor = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


  private[caching] def getViewCacheState(par: V#Par): ViewCacheState[V] = ???

  //
  // TODO implement this
  //  what should this do ?
  //
  //

  private[this] var map: Map[V#Par, ViewCacheState[V]] = Map()

  private[this] def loadViewResultIntoCache(par: V#Par) (implicit
      decoder: Decoder[V#Res],
      encoder: Encoder[V#Par],
      ct:      ClassTag[V] ): Unit = {

    implicit def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    getViewFromServer[V](View_AJAX_Request_Params( par )).onComplete(
      r  => {
        r.foreach( decoded => { this.map = map + (decoded.par -> ViewLoaded( decoded.par, decoded.res ) )} )
        if (!map.valuesIterator.exists( _.isLoading )) cacheInterface.reRenderShouldBeTriggered()
      }
    )

  }

  def prettyPrint( aMap: Map[_, _] ): String = aMap.foldLeft( "" ) {
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

  private[this] def getViewReqResultOrExecuteAction(par:V#Par)( action: => Unit ): ViewCacheState[V] = {
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

  private[this] def insertIntoCacheAsLoading( par: V#Par ): ViewLoading[V] = {

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
