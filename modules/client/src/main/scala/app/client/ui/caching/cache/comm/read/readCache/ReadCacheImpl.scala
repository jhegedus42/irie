package app.client.ui.caching.cache.comm.read.readCache

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cache.ReadCacheEntryStates.{
  InFlight,
  ReadCacheEntryState,
  Returned,
  Stale
}
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.AJAXCalls.{
  AjaxCallPar,
  sendPostAjaxRequest
}
import app.client.ui.caching.cache.comm.read.readCache.invalidation.{
  Invalidator,
  InvalidatorStream
}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.{PostRequest, ReadRequest}
import cats.syntax.writer
import io.circe.{Decoder, Encoder}
import sodium.Stream

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag
import scala.util.Try

private[caching] class ReadCacheImpl[Req <: PostRequest[ReadRequest]](
)(
  implicit invalidator: Invalidator[Req],
  decoder:              Decoder[Req#ResT],
  encoder:              Encoder[Req#ParT],
  ct:                   ClassTag[Req],
  ct2:                  ClassTag[Req#PayLoadT])
    extends ReadCache[Req] {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  protected[this] var map: Map[Req#ParT, ReadCacheEntryState[Req]] =
    Map()

  override def clearCache(): Unit = { map = Map() }

  invalidator.listen(
    () => map,
    x  => map = x,
    () => { ReRenderer.triggerReRender() }
  )

  def getInFlight(param: Req#ParT): InFlight[Req] = {

    val loading = InFlight[Req](param)
    this.map = map + (param -> loading)

    sendPostAjaxRequest[Req](AjaxCallPar(param))
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
  }

  override def getRequestResult(
    par: Req#ParT
  ): ReadCacheEntryState[Req] = {

    if (!map.contains(par)) {
      getInFlight(par)
    } else {
      val res: ReadCacheEntryState[Req] = map(par)

      val res2 = res match {
        case InFlight(param)                      => res
        case Returned(param, result)              => res
        case Stale(param, result)                 => getInFlight(par)
        case ReadCacheEntryStates.TimedOut(param) => res
        case ReadCacheEntryStates
              .ReturnedWithError(param, descriptionOfError) =>
          res
      }

      res2
    }
  }

}
