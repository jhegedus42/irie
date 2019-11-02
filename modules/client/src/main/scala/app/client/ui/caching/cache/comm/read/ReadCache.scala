package app.client.ui.caching.cache.comm.read

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cache.ReadCacheEntryStates.{InFlight, ReadCacheEntryState, Returned, Stale}
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import app.client.ui.caching.cache.comm.read.invalidation.ReadCacheInvalidatorStream
import app.client.ui.caching.cache.comm.write.{WriteAjaxReturnedStream, WriteRequestHandlerTC}
import app.client.ui.caching.cache.comm.write.WriteAjaxReturnedStream.Payload
import app.client.ui.caching.cacheInjector.ReRenderer
import app.client.ui.caching.cacheInjector.ReRenderer.ReRenderTriggerer
import app.shared.comm.postRequests.read.GetAllUsersReq
import app.shared.comm.postRequests.{GetEntityReq, GetLatestEntityByIDReq, GetUsersNotesReq, SumIntRoute, UpdateReq}
import app.shared.comm.{PostRequest, ReadRequest, WriteRequest, postRequests}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.{RefToEntityByID, RefToEntityWithVersion}
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.{Decoder, Encoder}
import sodium.StreamSink
import sodium._

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag
import scala.util.Try

trait ReadCache[Req <: PostRequest[ReadRequest]] {
  private[caching] def getRequestResult(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): ReadCacheEntryState[Req]

//  private[caching] def setEntryToStale(
//    par: Req#ParT
//  )(
//    implicit
//    decoder: Decoder[Req#ResT],
//    encoder: Encoder[Req#ParT],
//    ct:      ClassTag[Req],
//    ct2:     ClassTag[Req#PayLoadT]
//  ): Unit

  def clearCache(): Unit
}

private[caching] class ReadCacheImpl[Req <: PostRequest[ReadRequest]](
)(
  implicit val invalidator: Option[ReadCacheInvalidatorStream[Req]])
    extends ReadCache[Req] {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  protected[this] var map: Map[Req#ParT, ReadCacheEntryState[Req]] =
    Map()

  override def clearCache(): Unit = { map = Map() }

//  val clearCacheStream = new StreamSink[Unit]()

  if (invalidator.isDefined) {
    val in: Stream[Req#ParT] = invalidator.get.stream

    in.listen(x => { setElementToStale(x) })

  }

  private def setElementToStale(p: Req#ParT): Unit = {

    val oldVal: Option[ReadCacheEntryState[Req]] = map.get(p)
    if (oldVal.isDefined) {
      val newMap = map + (p -> oldVal.get.toStale.get)
      map=newMap
      ReRenderer.triggerReRender()
    }

  }

  def getInFlight(
    param: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): InFlight[Req] = {

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
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
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

object Invalidation{

  sealed trait InvalidationType
  case class SingleEntry() extends InvalidationType
  case class FullReset() extends InvalidationType


  def getInvalidator[
    WriteReq <: PostRequest[WriteRequest],
    ReadReq  <: PostRequest[ReadRequest]
  ](ws: WriteAjaxReturnedStream[WriteReq],
    f:  Payload[WriteReq] => ReadReq#ParT
  ): Option[ReadCacheInvalidatorStream[ReadReq]] =
    Some(new ReadCacheInvalidatorStream[ReadReq](ws.getStream.map(f)))
}


object ReadCache {

  import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTC._

  implicit val getLatestUserCache = {
    implicit val cacheInvalidatorStream =
      Invalidation.getInvalidator[UpdateReq[User], GetLatestEntityByIDReq[User]](
        WriteRequestHandlerTC.userUpdater.writeAjaxReturnedStream, {
          p: Payload[UpdateReq[User]] =>
            GetLatestEntityByIDReq.Par[User](
              p.par.currentEntity.toRef.entityIdentity
            )
        }
      )
    new ReadCacheImpl[GetLatestEntityByIDReq[User]]()
  }

  implicit def getEntityReqInvaliator[V <: EntityType[V]](
    implicit wrh: WriteRequestHandlerTC[UpdateReq[V]]
  ): Option[ReadCacheInvalidatorStream[GetEntityReq[V]]] = {
    def f: Payload[UpdateReq[V]] => GetEntityReq[V]#ParT = {
      ur: Payload[UpdateReq[V]] =>
        val r1: RefToEntityWithVersion[V] = ur.par.currentEntity.toRef
        GetEntityReq.Par[V](r1)
    }
    Invalidation.getInvalidator[UpdateReq[V], GetEntityReq[V]](
      wrh.writeAjaxReturnedStream,
      f
    )
  }

  implicit val getUserCache =
    new ReadCacheImpl[GetEntityReq[User]]

  implicit val getAllUsersReqCache = {
    implicit val x = None //todo-now continue here
    // create user triggers this
    // create a clear cache on any user change
    // use Full Reset
    new ReadCacheImpl[GetAllUsersReq]
  }

  implicit val getAllNotesCache = {
    implicit val x = None //todo-later
    // create note, triggers this
    new ReadCacheImpl[GetUsersNotesReq]
  }

  implicit val getNoteCache = new ReadCacheImpl[GetEntityReq[Note]]

}
