package app.client.ui.caching.cache.comm

import AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import app.client.ui.caching.cache.CacheEntryStates.{
  CacheEntryState,
  Loaded,
  Loading
}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.{PostRequest, PostRequestType, ReadRequest}
import app.shared.comm.postRequests.{
  GetAllUsersReq,
  GetEntityReq,
  SumIntRoute
}
import app.shared.entity.entityValue.values.User
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

trait PostRequestResultCache[
  RT  <: PostRequestType,
  Req <: PostRequest[RT]] {
  private[caching] def getPostRequestResult(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): CacheEntryState[RT, Req]

}

// todo-now :
//  use different implementations of cache-s
//  1) for read type requests
//  and
//  2) for write type requests
//
//  the current implementaiton uses the same type of cache
//  for these two types of - fundamentally - different requests
//
//  => this should be a trait and not a class

private[caching] class PostRequestResultCacheImpl[
  RT  <: PostRequestType,
  Req <: PostRequest[RT]
]() extends PostRequestResultCache [RT,Req] {


  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private[this] var map: Map[Req#ParT, CacheEntryState[RT, Req]] =
    Map()

  override def getPostRequestResult(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): CacheEntryState[RT, Req] =
    if (!map.contains(par)) {
      val loading = Loading[RT, Req](par)
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
  implicit val sumIntPostRequestResultCache =
    new PostRequestResultCacheImpl[ReadRequest, SumIntRoute]()

  implicit val getUserCache =
    new PostRequestResultCacheImpl[ReadRequest, GetEntityReq[User]]()

  implicit val getAllUsersReqCache =
    new PostRequestResultCacheImpl[ReadRequest, GetAllUsersReq]()
}
