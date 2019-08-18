package app.client.ui.caching.viewCache

import app.client.ui.caching.viewCache.REST_ForView.{View_AJAX_Request_Params, getViewFromServer}
import app.client.ui.caching.viewCache.ViewCacheStates.{ViewCacheState, ViewLoaded, ViewLoading}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.views.PostRequest
import app.shared.dataModel.views.SumIntPostRequest
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

private[caching] class ViewCache[V <: PostRequest]( ) {

  implicit def executionContext: ExecutionContextExecutor = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private[this] var map: Map[V#Par, ViewCacheState[V]] = Map()

  private[caching] def getViewCacheState(par:V#Par) (implicit
      decoder: Decoder[V#Res],
      encoder: Encoder[V#Par],
      ct:      ClassTag[V] )  : ViewCacheState[V] =
      if (!map.contains( par )) {
        val loading = ViewLoading( par )
        this.map = map + (par -> loading)
        getViewFromServer[V](View_AJAX_Request_Params( par )).onComplete(
          r  => {
            r.foreach( decoded => { this.map = map + (decoded.par -> ViewLoaded( decoded.par, decoded.res ) )} )
            if (!map.valuesIterator.exists( _.isLoading )) ReRenderer.triggerReRender()
          }
        )
        loading
      }
      else map( par )
  }

object ViewCache {
  implicit val sumIntViewCache = new ViewCache[SumIntPostRequest]( )
}
