package app.client.ui.caching.viewCache

import app.client.ui.caching.entityCache.ReRenderTriggererHolderSingletonGloballyAccessibleObject
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView

import scala.concurrent.ExecutionContextExecutor
import scala.util.Try
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{
  ViewHttpRouteName,
  ViewHttpRouteNameProvider
}
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.reflect.ClassTag


object SumIntViewCache {
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


  var sumIntViewOpt: Option[( SumIntView#Par, SumIntView#Res )] = None

  def getSumIntView(requestParams: SumIntView#Par ): Option[SumIntView#Res] = {

    import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView
    import io.circe.generic.auto._


    postViewRequest[SumIntView]( requestParams )
      .onComplete( (res: Try[SumIntView_HolderObject.SumIntView_Res]) => {

        //
        // TODO 1:
        // we set here  `sumIntViewOpt` to "Loaded" - if Try is a "Success"
        //
        // then we re-render the react page with :
        ReRenderTriggererHolderSingletonGloballyAccessibleObject.triggerReRender()

      } )

    sumIntViewOpt.map(_._2) // this is incorrect, we need to fix this

    // TODO 2 : -- see above, we need 3 states for `sumIntViewOpt`
    //
    // similarly to `EntityCacheStates` :
    //
    // NotQueriedYetFromServer, Loading, Loaded
    // Depending on the content of `sumIntViewOpt`, based on these states
    // we either
    //
    //    1) if `sumIntViewOpt` is "NotQueriedYetFromServer" then
    //       a) launch an ajax request
    //       b) set the state of `sumIntViewOpt` to Loading
    //       c) return "Loading"
    //
    //    2) if `sumIntViewOpt` is "Loading" then
    //       we do not launch no request and return "Loading"
    //
    //    3) if `sumIntViewOpt` is "Loaded then
    //       we do not launch no ajax request and return "Loaded"

  }


  def postViewRequest[V <: View](
      requestParams: V#Par
    )(
      implicit ct: ClassTag[V],
      encoder:     Encoder[V#Par],
      decoder:     Decoder[V#Res]
    ): Future[V#Res] = {

    val routeName: ViewHttpRouteName =
      ViewHttpRouteNameProvider.getViewHttpRouteName[V]()

    val url: String = routeName.name

    val json_line: String = requestParams.asJson.spaces2 // encode

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )

    val res: Future[V#Res] = Ajax
      .post( url, json_line, headers = headers )
      .map( _.responseText )
      .map( (x: String) => { decode[V#Res]( x ) } )
      .map( x => x.right.get )

    res
  }

}
//
//private[hidden] class MapForViewCache[E <: Entity]() {
//  private var map: Map[TypedRef[E], CacheState[E]] = Map()
//
//  def getCacheContentAsPrettyString: String =
//    map.foldLeft( "" )( ( s, t ) => s"$s\n$t\n" )
//
//  def isAjaxReqStillPending: Boolean = {
//    val res = map
//      .valuesIterator.exists(
//        (x: CacheState[E]) => x.isLoading
//      )
//    res
//  }
//
//  def insertIntoCacheAsLoaded(rv: RefVal[E] ): Unit = {
//    println(
//      s"CACHE WRITE => we insert $rv into the cache"
//    )
//    //    logger.trace(s"parameter:$rv")
//    val map2 = map + (rv.r -> Loaded( rv.r, rv ))
//    this.map = map2
//  }
//
//  def getEntityOrExecuteAction(
//      ref: TypedRef[E]
//    )(
//      action: => Unit
//    ): CacheState[E] = {
//
//    val res: CacheState[E] = if (!map.contains( ref )) {
//      val loading = Loading( ref )
//      println(
//        s"getEntityOrExecuteAction, " +
//          s"map = $map, " +
//          s"loading = $loading," +
//          s" ref=$ref"
//      )
//      insertIntoCacheAsLoading( ref )
//      action
//      loading
//    } else map( ref )
//    res
//  }
//
//  //  implicit override def executionContext =
////    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
//
//  def insertIntoCacheAsLoading(r: TypedRef[E] ): Loading[E] = {
//
//    println( s"CACHE WRITE => we insert $r into the cache" )
//    //    logger.trace(s"parameter:$rv")
//
//    val v = Loading( r )
//
//    val map2 = map + (r -> v)
//    this.map = map2
//    v
//  }
//
//}
//
///**
//  * This is a map that contains cached Entities.
//  *
//  * @tparam E
//  */
//private[entityCache] class ViewCache[E <: Entity](cacheInterface: CacheInterface ) {
//  println( s"Constructor of EntityCacheMap" )
//
//  val cacheMap = new MapForCache[E]
//
//  var nrOfAjaxReqSent = 0
//  var nrOfAjaxReqReturnedAndHandled = 0
//
//  private[entityCache] def readEntity(
//      refToEntity: TypedRef[E]
//    )(
//      implicit
//      decoder: Decoder[RefVal[E]],
//      ct:      ClassTag[E]
//    ): CacheState[E] = {
//
//    println( s"par: $refToEntity" )
//
//    val res =
//      cacheMap.getEntityOrExecuteAction( refToEntity ) {
//        launchReadAjax( refToEntity )
//      }
//
//    res
//
//  }
//
//  private def launchReadAjax(
//      ref: TypedRef[E]
//    )(
//      implicit decoder: Decoder[RefVal[E]],
//      ct:               ClassTag[E]
//    ): Unit = {
//
//    nrOfAjaxReqSent = nrOfAjaxReqSent + 1
//
//    println(
//      s"LOGGER launchReadAjax, nrOfAjaxReqSent= $nrOfAjaxReqSent, " +
//        s"nrOfAjaxReqReturnedAndHandled= $nrOfAjaxReqReturnedAndHandled"
//    )
//
//    implicit def executionContext: ExecutionContextExecutor =
//      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
//
//    val ajaxCallAsFuture: Future[RefVal[E]] = {
//      val res: Future[RefVal[E]] = getEntity[E]( ref )
//      res
//    }
//
//    ajaxCallAsFuture.onComplete(
//      r => ajaxReqReturnHandler( r )
//    )
//  }
//
//  private def ajaxReqReturnHandler(tryRefVal: Try[RefVal[E]] ): Unit = {
//    println(
//      s"ajaxReqReturnHandler called with parameter $tryRefVal"
//    )
//
//    tryRefVal.foreach(
//      rv => cacheMap.insertIntoCacheAsLoaded( rv )
//    )
//
//    println(
//      s"isAjaxReqStillPending=${cacheMap.isAjaxReqStillPending}, at ajaxReqReturnHandler"
//    )
//
//    if (!cacheMap.isAjaxReqStillPending) { //we trigger a re-render if this is the "last ajax request that came back"
//      println( s"LAST AJAX call returned => re-render needs to be triggered" )
//      cacheInterface.reRenderShouldBeTriggered()
//    }
//
//    nrOfAjaxReqReturnedAndHandled = nrOfAjaxReqReturnedAndHandled + 1
//
//    println(
//      s"LOGGER ajaxReqReturnHandler, nrOfAjaxReqSent= $nrOfAjaxReqSent, " +
//        s"nrOfAjaxReqReturnedAndHandled= $nrOfAjaxReqReturnedAndHandled"
//    )
//  }
//
//}
