package app.client.ui.caching.viewCache

import app.client.ui.caching.viewCache.ViewCacheStates.{ViewCacheState, ViewLoaded, ViewLoading}
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.shared.utils.logging.LoggingHelperFunctions

private[caching] class MapForViewCache[V <: View]() {
  private var map: Map[V#Par, ViewCacheState[V]] = Map()

  def getCacheContentAsPrettyString: String =
    map.foldLeft( "" )( ( s, t ) => s"$s\n$t\n" )

  def isAjaxReqStillPending: Boolean = {
    val res = map.valuesIterator.exists(
      (x: ViewCacheState[V]) => x.isLoading
    )
    res
  }

  def insertIntoCacheAsLoaded(par: V#Par, res: V#Res ): ViewLoaded[V] = {


    val toBeInsertedIntoCache: ViewLoaded[V] =ViewLoaded( par, res )

    val cacheBeforeInsertion=map

    val cacheAfterInsertion= cacheBeforeInsertion + (par -> toBeInsertedIntoCache)

    this.map = cacheAfterInsertion

    LoggingHelperFunctions.logMethodCall(
      "insertIntoCacheAsLoaded(par: V#Par, res: V#Res )",
      s"""
        |
        |
        | we inserted $toBeInsertedIntoCache into the cache, which had
        |
        | the following content before insertion :
        | $cacheBeforeInsertion
        |
        |
        | and it has following content after insertion :
        | $cacheAfterInsertion
        |
        |
      """.stripMargin)

    toBeInsertedIntoCache
  }

  def insertIntoCacheAsLoading(par: V#Par ): ViewLoading[V] = {

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
    this.map   = newMap

    vl

  }

  def getViewReqResultOrExecuteAction(par: V#Par)(action: => Unit): ViewCacheState[V] = {

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

}
