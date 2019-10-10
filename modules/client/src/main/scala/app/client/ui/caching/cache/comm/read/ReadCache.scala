package app.client.ui.caching.cache.comm.read

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cache.ReadCacheEntryStates.{InFlight, ReadCacheEntryState, Returned, Stale}
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.postRequests.{GetAllUsersReq, GetEntityReq, GetLatestEntityByIDReq, SumIntRoute}
import app.shared.comm.{PostRequest, ReadRequest}
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.{RefToEntityByID, RefToEntityWithVersion}
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

  private[caching] def setEntryToStale(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): Unit

  def clearCache() : Unit
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

  override  def clearCache() : Unit = {map= Map()}

  def getInFlight(
    param: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): InFlight[RT, Req] = {

    val loading = InFlight[RT, Req](param)
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
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): ReadCacheEntryState[RT, Req] = {

    if (!map.contains(par)) {
      getInFlight(par)
    } else {
      val res: ReadCacheEntryState[RT, Req] = map(par)

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

  override private[caching] def setEntryToStale(
    par: Req#ParT
  )(
    implicit decoder: Decoder[Req#ResT],
    encoder:          Encoder[Req#ParT],
    ct:               ClassTag[Req],
    ct2:              ClassTag[Req#PayLoadT]
  ): Unit = {

    println(s"map: $map")

    println(s"map's keys:\n")

    map.keys.foreach(println)

    println("8F3C7E78-00BB-4B7E-A503-FAB20E1BA3C6 - " +
      "app.client.ui.caching.cache.comm.read.ReadCacheImpl.setEntryToStale " +
      "was called.")

    println(s"par: $par")

    val oldVal: ReadCacheEntryState[RT, Req] = map(par)

    oldVal match {
      case InFlight(param)                      =>
      case Returned(param, result)              => {

        val newMap = map + (par -> oldVal.toStale.get)

        map = newMap


        ReRenderer.triggerReRender()

      }
      case Stale(param, result)                 =>
      case ReadCacheEntryStates.TimedOut(param) =>
      case ReadCacheEntryStates
            .ReturnedWithError(param, descriptionOfError) =>
    }

  }

}

object ReadCache {

  implicit val getLatestUserCache =
    new ReadCacheImpl[ReadRequest, GetLatestEntityByIDReq[User]]() {

      def invalidateEntry(par: RefToEntityByID[User]): Unit = {
        println(
          "9CF6BB1D-469A-4764-9521-7A8D335A85CB - invalidateEntry() was called in " +
            "app.client.ui.caching.cache.comm.read.ReadCache.getLatestUserCache"
        )
        val p=GetLatestEntityByIDReq.Par(par)
        setEntryToStale(p)
        getAllUsersReqCache.clearCache()
      }

//      def invalidateEntry_old(par: RefToEntityWithVersion[User]): Unit = {
//        println(
//          "9CF6BB1D-469A-4764-9521-7A8D335A85CB - invalidateEntry() was called in " +
//            "app.client.ui.caching.cache.comm.read.ReadCache.getLatestUserCache"
//        )
//
//        val id = par.entityIdentity
//
//        val keys_ = map.keys
//
//        val key = keys_.filter(
//          p => p.refToEntityWithVersion.entityIdentity == id
//        )
//        val oldVal = map(key.head)
//
//        val newMap = map + (key.head -> oldVal.toStale.get)
//
//        map = newMap
//
//        ReRenderer.triggerReRender()
//
//      }
    }

  implicit val sumIntPostRequestResultCache =
    new ReadCacheImpl[ReadRequest, SumIntRoute]()

  implicit val getUserCache =
    new ReadCacheImpl[ReadRequest, GetEntityReq[User]]() {

//      def invalidateEntry(par: RefToEntityWithVersion[User]): Unit = {
//        val id    = par.entityIdentity
//        val keys_ = map.keys
//        val key = keys_.filter(
//          p => p.refToEntityWithVersion.entityIdentity == id
//        )
//        val oldVal = map(key.head)
//        val newMap = map + (key.head -> oldVal.toStale.get)
//        ReRenderer.triggerReRender()
//      }

    }

  implicit val getAllUsersReqCache =
    new ReadCacheImpl[ReadRequest, GetAllUsersReq]()

}
