package app.client.ui.caching.viewCache

import app.client.ui.caching.entityCache.EntityCacheStates.{EntityCacheState, Loaded, Loading}
import app.client.ui.caching.viewCache.ViewCacheStates.{ViewCacheState, ViewLoaded}
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{RefVal, TypedRef}

private[caching] class MapForViewCache[V <: View]() {
  private var map: Map[V#Par, ViewCacheState[V]] = Map()

  def getCacheContentAsPrettyString: String =
    map.foldLeft( "" )( ( s, t ) => s"$s\n$t\n" )

  def isAjaxReqStillPending: Boolean = {
    val res = map
      .valuesIterator.exists(
        (x: ViewCacheState[V]) => x.isLoading
      )
    res
  }

  def insertIntoCacheAsLoaded(rv: ViewLoaded[V] ): Unit = {
    println(
      s"CACHE WRITE => we insert $rv into the cache"
    )
    //    logger.trace(s"parameter:$rv")
    val map2 = map + (rv.r -> Loaded( rv.r, rv ))
    this.map = map2
  }

  def getEntityOrExecuteAction(
      ref: TypedRef[V]
    )(
      action: => Unit
    ): EntityCacheState[V] = {

    val res: EntityCacheState[V] = if (!map.contains( ref )) {
      val loading = Loading( ref )
      println(
        s"getEntityOrExecuteAction, " +
          s"map = $map, " +
          s"loading = $loading," +
          s" ref=$ref"
      )
      insertIntoCacheAsLoading( ref )
      action
      loading
    } else map( ref )
    res
  }

  def insertIntoCacheAsLoading(r: TypedRef[V] ): Loading[V] = {

    println( s"CACHE WRITE => we insert $r into the cache" )
    //    logger.trace(s"parameter:$rv")

    val v = Loading( r )

    val map2 = map + (r -> v)
    this.map = map2
    v
  }

}
