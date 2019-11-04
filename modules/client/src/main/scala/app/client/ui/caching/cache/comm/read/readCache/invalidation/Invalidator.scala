package app.client.ui.caching.cache.comm.read.readCache.invalidation

import app.client.ui.caching.cache.ReadCacheEntryStates.ReadCacheEntryState
import app.client.ui.caching.cache.comm.write.WriteAjaxReturnedStream.Payload
import app.client.ui.caching.cache.comm.write.{
  WriteAjaxReturnedStream,
  WriteRequestHandlerTC
}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.postRequests.read.GetAllUsersReq
import app.shared.comm.postRequests.{
  CreateEntityReq,
  GetEntityReq,
  GetLatestEntityByIDReq,
  GetUsersNotesReq,
  UpdateReq
}
import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.RefToEntityWithVersion
import sodium.{Stream, StreamSink}
import WriteRequestHandlerTC._

trait Invalidator[Req <: PostRequest[ReadRequest]] {
  type M = Map[Req#ParT, ReadCacheEntryState[Req]]

  def listen(
    g: () => M,
    f: M => Unit,
    h: () => Unit
  ): Unit

  def setElementToStale(
    p:      Req#ParT,
    oldMap: M
  ): M = {
    val oldVal: Option[ReadCacheEntryState[Req]] = oldMap.get(p)
    if (oldVal.isDefined) {
      val newMap = oldMap + (p -> oldVal.get.toStale.get)
      newMap
    } else oldMap
  }

}

object Invalidator {

  def generalInvalidator[
    Read  <: PostRequest[ReadRequest],
    Write <: PostRequest[WriteRequest]
  ](
  )(
    implicit wc: WriteRequestHandlerTC[Write],
    adapter:     Adapter[Read, Write]
  ): Invalidator[Read] = {
    type R = Read
    type W = Write

    new Invalidator[R] {

      val s1: Stream[Read#ParT] = wc.stream.map(adapter.write2read)

      override def listen(
        g: () => M,
        f: M => Unit,
        h: () => Unit
      ): Unit = {
        s1.listen({ (x: Read#ParT) =>
          val oldMap: M = g()
          val newMap = setElementToStale(x, oldMap)
          f(newMap)
          h()
        })
      }
    }
  }

  implicit def getUsersNotesReq(
    implicit wc: WriteRequestHandlerTC[UpdateReq[Note]]
  ): Invalidator[GetUsersNotesReq] =
    new Invalidator[GetUsersNotesReq] {
      val s1: StreamSink[UpdateReq.UpdateReqPar[Note]] = wc.stream

      override def listen(
        g: () => M,
        f: M => Unit,
        h: () => Unit
      ): Unit = {
        val m: M = g()
        val newMap = m.empty
        s1.listen({ (_) =>
          f(newMap)
          h()
        })
      }
    }

  implicit def getLatestEntityByIDReq
    : Invalidator[GetLatestEntityByIDReq[User]] =
    generalInvalidator[GetLatestEntityByIDReq[User], UpdateReq[
      User
    ]]()

  implicit def getEntityInvalidator[V <: EntityType[V]](
    implicit wc: WriteRequestHandlerTC[UpdateReq[V]]
  ) = { generalInvalidator[GetEntityReq[V], UpdateReq[V]]() }


  implicit def getAllUsersReqInvalidator
    : Invalidator[GetAllUsersReq] =
    generalInvalidator[GetAllUsersReq, UpdateReq[User]]()

  // todo-now add invalidator for creating user

}
