package app.client.ui.caching.cache.comm

import AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import app.client.ui.caching.cache.CacheEntryStates.{CacheEntryState, Loaded, Loading}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.{PostRequest, PostRequestType, ReadRequest}
import app.shared.comm.postRequests.{GetAllUsersReq, GetEntityReq, SumIntRoute}
import app.shared.entity.entityValue.values.User
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

private[caching] class PostRequestResultCache[RT<:PostRequestType, Req<: PostRequest[RT]]() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private[this] var map: Map[Req#ParT, CacheEntryState[RT,Req]] = Map()

  private[caching] def getPostRequestResult(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): CacheEntryState[RT,Req] =
    if (!map.contains(par)) {
      val loading = Loading[RT,Req](par)
//      this.map = map + (par -> loading) todo-now "fix this"
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
  implicit val sumIntPostRequestResultCache =
    new PostRequestResultCache[ReadRequest, SumIntRoute]()

  implicit val getUserCache =
    new PostRequestResultCache[ReadRequest, GetEntityReq[User]]()

  implicit val getAllUsersReqCache =
    new PostRequestResultCache[ReadRequest, GetAllUsersReq]()
}
