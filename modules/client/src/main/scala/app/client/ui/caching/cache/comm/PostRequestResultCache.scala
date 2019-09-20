package app.client.ui.caching.cache.comm

import AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import app.client.ui.caching.cache.CacheEntryStates.{CacheEntryState, Loaded, Loading}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.PostRequest
import app.shared.comm.postRequests.{GetAllUsersReq, GetEntityReq, SumIntRoute}
import app.shared.entity.entityValue.values.User
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

private[caching] class PostRequestResultCache[Req <: PostRequest]() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private[this] var map: Map[Req#ParT, CacheEntryState[Req]] = Map()

  private[caching] def getPostRequestResult(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
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
//            RouterWrapper.reRenderApp()
            // todo-later : try to simplify the app by uncommenting the line
            //  above and commenting out the line above the line above
            //  - problem : I tried it already and the "cache" "does not work"
            //    this mean : it does not refreshes the page when the AJAX comes
            //      back, so possibly the  `RouterWrapper.reRenderApp()` call
            //      does not actually triggers the child of the router to be
            //      be re-rendered
            //      - apparently only the current `ReRenderer.triggerReRender()`
            //        based solution works for that, which is perhaps overly
            //        complicated and not neccessary ...
            //        => need to think about how to throw out
            //           `ReRenderer.triggerReRender()`
            //           and replace it with `RouterWrapper.reRenderApp()`
            //           but this is not super high priority, but the
            //           unneccessary extra complexity due to
            //           `ReRenderer.triggerReRender()`
            //           starts to be more and more annoying ...
            //           say, exponentially more and
            //           more


          }
        )
      loading
    } else map(par)
}

object PostRequestResultCache {
  implicit val sumIntPostRequestResultCache =
    new PostRequestResultCache[SumIntRoute]()

  implicit val getUserCache =
    new PostRequestResultCache[GetEntityReq[User]]()

  implicit val getAllUsersReqCache =
    new PostRequestResultCache[GetAllUsersReq]()
}
