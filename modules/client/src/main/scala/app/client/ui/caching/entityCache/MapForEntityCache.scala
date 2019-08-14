package app.client.ui.caching.entityCache

import app.client.ui.caching.entityCache.EntityCacheStates.{EntityCacheState, Loaded, Loading}
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.{TypedRefToEntity, Entity}

private[entityCache] class MapForEntityCache[E <: EntityValue[E]]() {
  private[this] var map: Map[TypedRefToEntity[E], EntityCacheState[E]] = Map()

//  def getCacheContentAsPrettyString: String =
//    map.foldLeft( "" )( ( s, t ) => s"$s\n$t\n" )

  def isAjaxReqStillPending: Boolean = {
    val res = map
      .valuesIterator.exists(
        (x: EntityCacheState[E]) => x.isLoading
      )
    res
  }

  def insertIntoCacheAsLoaded(rv: Entity[E] ): Unit = {
    println(
      s"CACHE WRITE => we insert $rv into the cache"
    )
    //    logger.trace(s"parameter:$rv")
    val map2 = map + (rv.typedRefToEntity -> Loaded( rv.typedRefToEntity, rv ))
    this.map = map2
  }

  def getEntityOrExecuteAction(
      ref: TypedRefToEntity[E]
    )(
      action: => Unit
    ): EntityCacheState[E] = {

    val res: EntityCacheState[E] = if (!map.contains( ref )) {
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

  private[this] def insertIntoCacheAsLoading(r: TypedRefToEntity[E] ): Loading[E] = {

    println( s"CACHE WRITE => we insert $r into the cache" )
    //    logger.trace(s"parameter:$rv")

    val v = Loading( r )

    val map2 = map + (r -> v)
    this.map = map2
    v
  }

}
