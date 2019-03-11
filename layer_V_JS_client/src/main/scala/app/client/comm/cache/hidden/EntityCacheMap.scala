package app.client.comm.cache.hidden

import app.client.comm.REST.getEntity
import app.client.comm.cache.exposed.CacheInterface
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{Ref, RefVal}
import app.client.comm.cache.exposed.CacheStates.{CacheState, Loaded, Loading}
import slogging.LazyLogging

import scala.util.Try
//import app.client.comm.cache.hidden._temp_disabled_ajax.AJAXGetEntityApi.InFlight_ReadEntity
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

/**
  * This is a map that contains cached Entities.
  *
  * @tparam E
  */
private[cache] class EntityCacheMap[E <: Entity]() extends LazyLogging{
  logger.trace("Constructor of EntityCacheMap")


  var map: Map[Ref[E], CacheState[E]] = Map()

  def getCacheContentAsPrettyString:String=map.foldLeft("")((s,t)=>s"$s\n$t\n")

  def isAjaxReqStillPending: Boolean = {
    val res = map.valuesIterator.exists( (x: CacheState[E]) => x.isLoading )
    res
  }

  def insertIntoCache(rv:RefVal[E])={
    println(s"CACHE WRITE => we insert $rv into the cache")
    logger.trace(s"parameter:$rv")
    val map2=map + ( rv.r -> Loaded(rv.r,rv))
    this.map=map2
  }

  def ajaxReqReturnHandler(r: Try[RefVal[E]] ): Unit = {
    logger.trace(s"ajaxReqReturnHandler called with parameter $r")

    r.foreach(insertIntoCache)

    if (!isAjaxReqStillPending) { //we trigger a re-render if this is the "last ajax request that came back"
      logger.trace( "LAST AJAX call returned => re-render needs to be triggered" )
      val res=CacheInterface.reRenderTriggerer.flatMap( x => {
        x.triggerReRender()
        Some("successfully re-rendered")
      } )
    }

  }

  def launchReadAjax(ref: Ref[E] )(implicit decoder: Decoder[RefVal[E]], ct: ClassTag[E] ): Unit = {
    logger.trace(s"par: $ref")
    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val ajaxCallAsFuture: Future[RefVal[E]] = {
      val res: Future[RefVal[E]] = getEntity[E]( ref )
      res
    }

    ajaxCallAsFuture.onComplete( r => ajaxReqReturnHandler( r ) )
  }

  def readEntity(
      refToEntity: Ref[E]
    )(
      implicit
      decoder: Decoder[RefVal[E]],
      ct:      ClassTag[E]
    ): CacheState[E] = { // 74291aeb_02f0aea6
    logger.trace(s"par: $refToEntity")
    if (!map.contains( refToEntity )) {
      val loading = Loading( refToEntity )
      val res: Unit = launchReadAjax( refToEntity )
      loading //INPROGRESS => update the cache to LOADING
    } else map( refToEntity )
  }
}
