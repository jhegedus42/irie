package app.client.ui.caching.cache.comm.read

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
import app.client.ui.caching.cache.comm.read.invalidation.ReadCacheInvalidator
import app.client.ui.caching.cache.comm.write.WriteAjaxReturnedStream
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.postRequests.read.GetAllUsersReq
import app.shared.comm.postRequests.{
  GetEntityReq,
  GetLatestEntityByIDReq,
  GetUsersNotesReq,
  SumIntRoute,
  UpdateReq
}
import app.shared.comm.{
  PostRequest,
  ReadRequest,
  WriteRequest,
  postRequests
}
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.{
  RefToEntityByID,
  RefToEntityWithVersion
}
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
  val invalidator: Option[ReadCacheInvalidator[Req]])
    extends ReadCache[Req] {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  protected[this] var map: Map[Req#ParT, ReadCacheEntryState[Req]] =
    Map()

  override def clearCache(): Unit = { map = Map() }

//  val clearCacheStream = new StreamSink[Unit]()

  if (invalidator.isDefined) {
    val in: Stream[Req#ParT] = invalidator.get.getStream
    in.listen(x => setElementToStale(x))
  }

  private def setElementToStale(p: Req#ParT): Unit = {

    val oldVal: Option[ReadCacheEntryState[Req]] = map.get(p)
    if (oldVal.isDefined) {
      val newMap = map + (p -> oldVal.get.toStale.get)
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
//  override private[caching] def setEntryToStale(
//    par: Req#ParT
//  )(
//    implicit decoder: Decoder[Req#ResT],
//    encoder:          Encoder[Req#ParT],
//    ct:               ClassTag[Req],
//    ct2:              ClassTag[Req#PayLoadT]
//  ): Unit = {
//
//    println(s"map: $map")
//
//    println(s"map's keys:\n")
//
//    map.keys.foreach(println)
//
//    println(
//      "8F3C7E78-00BB-4B7E-A503-FAB20E1BA3C6 - " +
//        "app.client.ui.caching.cache.comm.read.ReadCacheImpl.setEntryToStale " +
//        "was called."
//    )
//
//    println(s"par: $par")
//
//    val oldVal: ReadCacheEntryState[RT, Req] = map(par)
//
//    oldVal match {
//      case InFlight(param) =>
//      case Returned(param, result) => {
//
//        val newMap = map + (par -> oldVal.toStale.get)
//
//        map = newMap
//
//        ReRenderer.triggerReRender()
//
//      }
//      case Stale(param, result)                 =>
//      case ReadCacheEntryStates.TimedOut(param) =>
//      case ReadCacheEntryStates
//            .ReturnedWithError(param, descriptionOfError) =>
//    }
//
//  }

}

//trait ReadCacheInvalidator[Req <: PostRequest[WriteRequest]] {}

object ReadCache {

  // todo-now CONTINUE HERE
  // put invalidators here

  import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTC._

  object GetLatestUserCacheInvalidator {
    val s1: WriteAjaxReturnedStream[UpdateReq[User]] = ???

    val inv1: ReadCacheInvalidator[GetLatestEntityByIDReq[User]] = ???

    def g(
      writeAjaxReturnedStream: WriteAjaxReturnedStream[
        UpdateReq[User]
      ]
    ): ReadCacheInvalidator[GetLatestEntityByIDReq[User]] = {

      val s: Stream[
        (UpdateReq.UpdateReqPar[User], UpdateReq.UpdateReqRes[User])
      ] = writeAjaxReturnedStream.getStream

      val si: StreamSink[GetLatestEntityByIDReq[User]#ParT] = ???

      ???
    }
  }

  implicit val getLatestUserCache =
    new ReadCacheImpl[GetLatestEntityByIDReq[User]](
      None
    )

  implicit val sumIntPostRequestResultCache =
    new ReadCacheImpl[SumIntRoute](None)

  implicit val getUserCache =
    new ReadCacheImpl[GetEntityReq[User]](None) {}

  implicit val getAllUsersReqCache =
    new ReadCacheImpl[GetAllUsersReq](None)

  implicit val getAllNotesCache =
    new ReadCacheImpl[GetUsersNotesReq](None)

  implicit val getNoteCache =
    new ReadCacheImpl[GetEntityReq[Note]](None)

}
