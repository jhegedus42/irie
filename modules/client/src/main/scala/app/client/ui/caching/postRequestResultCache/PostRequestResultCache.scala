package app.client.ui.caching.postRequestResultCache

import app.client.ui.caching.postRequestResultCache.PostRequestAJAXCalls.{
  View_AJAX_Request_Params,
  getPostRequestResultFromServer
}
import app.client.ui.caching.postRequestResultCache.PostRequestResultCacheEntryStates.{
  PostRequestResultCacheEntryState,
  PostRequestResultLoaded,
  PostRequestResultLoading
}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.requests.Request
import app.shared.dataModel.views.SumIntPostRequest
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

private[caching] class PostRequestResultCache[Req <: Request]() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private[this] var map: Map[Req#Par, PostRequestResultCacheEntryState[Req]] = Map()

  private[caching] def getPostRequestResultCacheState(par: Req#Par)(
    implicit
    decoder: Decoder[Req#Res],
    encoder: Encoder[Req#Par],
    ct: ClassTag[Req]
  ): PostRequestResultCacheEntryState[Req] =
    if (!map.contains(par)) {
      val loading = PostRequestResultLoading(par)
      this.map = map + (par -> loading)
      getPostRequestResultFromServer[Req](View_AJAX_Request_Params(par))
        .onComplete(
          r => {
            r.foreach(decoded => {
              this.map = map + (decoded.par -> PostRequestResultLoaded(
                decoded.par,
                decoded.res
              ))
            })
            if (!map.valuesIterator.exists(_.isLoading))
              ReRenderer.triggerReRender()
          }
        )
      loading
    } else map(par)
}

object PostRequestResultCache {
  implicit val sumIntPostRequestResultCache = new PostRequestResultCache[SumIntPostRequest]()
}
