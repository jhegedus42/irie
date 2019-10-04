package app.client.ui.caching.cache.comm.read

import app.client.ui.caching.cache.ReadCacheEntryStates.{InFlight, ReadCacheEntryState, Returned, Stale}
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.postRequests.{GetAllUsersReq, GetEntityReq, SumIntRoute}
import app.shared.comm.{PostRequest, ReadRequest}
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag
import scala.util.Try

trait ReadCache[RT <: ReadRequest, Req <: PostRequest[RT]] {
  private[caching] def getRequestResult(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): ReadCacheEntryState[RT, Req]

}


private[caching] class ReadCacheImpl[
  RT  <: ReadRequest,
  Req <: PostRequest[RT]]
    extends ReadCache[RT, Req] {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  protected[this] var map
    : Map[Req#ParT, ReadCacheEntryState[RT, Req]] =
    Map()

  override def getRequestResult(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): ReadCacheEntryState[RT, Req] =
    if (!map.contains(par)) {
      val loading = InFlight[RT, Req](par)
      this.map = map + (par -> loading)
      sendPostAjaxRequest[Req](AjaxCallPar(par))
        .onComplete(
          (r: Try[
            AJAXCalls.PostAJAXRequestSuccessfulResponse[Req]
          ]) => {
            r.foreach(decoded => {
              this.map = map + (decoded.par -> Returned(
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

object ReadCache {

  implicit val sumIntPostRequestResultCache =
    new ReadCacheImpl[ReadRequest, SumIntRoute]()

  implicit val getUserCache=
    new ReadCacheImpl[ReadRequest, GetEntityReq[User]]() {
      def invalidateEntry(
        par: RefToEntityWithVersion[User]
      ): Unit = {
        val id   = par.entityIdentity
        val keys_ = map.keys
        val key = keys_.filter(
          p => p.refToEntityWithoutVersion.entityIdentity  == id
        )
        val oldVal=map(key.head)
        val newMap=map + (key.head -> oldVal.toStale.get)

      }
    }

  implicit val getAllUsersReqCache =
    new ReadCacheImpl[ReadRequest, GetAllUsersReq]()

}
