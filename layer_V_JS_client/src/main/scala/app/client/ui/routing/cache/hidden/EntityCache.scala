package app.client.ui.routing.cache.hidden

import app.client.REST.getEntity
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{TypedRef, RefVal}
import app.client.ui.routing.cache.exposed.CacheInterface
import app.client.ui.routing.cache.exposed.CacheStates.{CacheState, Loaded, Loading}
import slogging.LazyLogging

import scala.util.Try
//import app.client.ui.components.cache.hidden._temp_disabled_ajax.AJAXGetEntityApi.InFlight_ReadEntity
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private [hidden] class MapForCache[E<:Entity]()extends LazyLogging {
  private var map: Map[TypedRef[E], CacheState[E]] = Map()

  def getCacheContentAsPrettyString:String=map.foldLeft("")((s,t)=>s"$s\n$t\n")

  def isAjaxReqStillPending: Boolean = {
    val res = map.valuesIterator.exists( (x: CacheState[E]) => x.isLoading )
    res
  }

  def insertIntoCacheAsLoading(r:TypedRef[E]) : Loading[E] = {

    println(s"CACHE WRITE => we insert $r into the cache")
    //    logger.trace(s"parameter:$rv")

    val v= Loading(r)

    val map2=map + ( r -> v )
    this.map=map2
    v
  }

  def insertIntoCacheAsLoaded(rv:RefVal[E]): Unit ={
    println(s"CACHE WRITE => we insert $rv into the cache")
//    logger.trace(s"parameter:$rv")
    val map2=map + ( rv.r -> Loaded(rv.r,rv))
    this.map=map2
  }

  def getEntityOrExecuteAction(ref:TypedRef[E])(action : => Unit):CacheState[E]={

    val res: CacheState[E] = if (!map.contains( ref )) {
      val loading = Loading( ref )
      logger.trace("ajax",map,loading,ref)
      insertIntoCacheAsLoading(ref)
      action
      loading
    } else map( ref )
    res
  }

}


/**
  * This is a map that contains cached Entities.
  *
  * @tparam E
  */
private[cache] class EntityCache[E <: Entity](cacheInterface: CacheInterface) extends LazyLogging{
  logger.trace("Constructor of EntityCacheMap")


  val cacheMap=new MapForCache[E]



  private def ajaxReqReturnHandler(tryRefVal: Try[RefVal[E]] ): Unit = {
    logger.trace(s"ajaxReqReturnHandler called with parameter $tryRefVal")

    tryRefVal.foreach(rv => cacheMap.insertIntoCacheAsLoaded(rv))

    println(s"isAjaxReqStillPending=${cacheMap.isAjaxReqStillPending}, at ajaxReqReturnHandler")

    if (!cacheMap.isAjaxReqStillPending) { //we trigger a re-render if this is the "last ajax request that came back"
      logger.trace( "LAST AJAX call returned => re-render needs to be triggered" )
      cacheInterface.reRenderShouldBeTriggered()
    }

  }

  private def launchReadAjax(ref: TypedRef[E] )(implicit decoder: Decoder[RefVal[E]], ct: ClassTag[E] ): Unit = {
    logger.trace(s"par: $ref")
    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val ajaxCallAsFuture: Future[RefVal[E]] = {
      val res: Future[RefVal[E]] = getEntity[E]( ref )
      res
    }

    ajaxCallAsFuture.onComplete( r => ajaxReqReturnHandler( r ) )
  }

  private[cache] def readEntity(
      refToEntity: TypedRef[E]
    )(
      implicit
      decoder: Decoder[RefVal[E]],
      ct:      ClassTag[E]
    ): CacheState[E] = {

    logger.trace(s"par: $refToEntity")


    val res=cacheMap.
      getEntityOrExecuteAction(refToEntity){launchReadAjax( refToEntity )}

    res

  }



}
