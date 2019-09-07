package app.client.ui.caching.cache

import app.client.ui.caching.cache.AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import app.client.ui.caching.cache.CacheEntryStates.{CacheEntryState, Loaded, Loading}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.PostRequest
import app.shared.comm.postRequests.SumIntRoute
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

private[caching] class PostRequestResultCache[Req <: PostRequest]() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private[this] var map: Map[Req#Par, CacheEntryState[Req]] = Map()

  private[caching] def getPostRequestResultCacheState(par: Req#Par)(
    implicit
    decoder: Decoder[Req#Res],
    encoder: Encoder[Req#Par],
    ct: ClassTag[Req],
    ct2:    ClassTag[Req#PayLoad]
  ): CacheEntryState[Req] =
    if (!map.contains(par)) {
      val loading = Loading(par)
      this.map = map + (par -> loading)
      sendPostAjaxRequest[Req](AjaxCallPar(par))
        .onComplete(
          r => {
            r.foreach(decoded => {
              this.map = map + (decoded.par -> Loaded(
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
  implicit val sumIntPostRequestResultCache = new PostRequestResultCache[SumIntRoute]()
}
