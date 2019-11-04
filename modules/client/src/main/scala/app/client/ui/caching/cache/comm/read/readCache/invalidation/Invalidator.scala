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
import app.shared.comm.{PostRequest, ReadRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import sodium.Stream

trait Invalidator[Req <: PostRequest[ReadRequest]] {
  type M = Map[Req#ParT, ReadCacheEntryState[Req]]

  def listen(
    g: () => M,
    f: M => Unit
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

  implicit def getUsersNotesReq: Invalidator[GetUsersNotesReq] = ???

  implicit def getLatestEntityByIDReq
    : Invalidator[GetLatestEntityByIDReq[User]] = ???

  implicit def getEntityInvalidator[
    V <: EntityType[V]
  ]: Invalidator[GetEntityReq[V]] = {
    type R = GetEntityReq[V]
    type W = UpdateReq[V]

    new Invalidator[R] {
      override def listen(
        g: () => M,
        f: M => Unit
      ): Unit = {}
    }

    // todo-now

  }

  implicit def getAllUsersReqInvalidator
    : Invalidator[GetAllUsersReq] =
    new Invalidator[GetAllUsersReq] {

      val w = implicitly[WriteRequestHandlerTC[UpdateReq[User]]]

      val s = w.stream

      val a = implicitly[Adapter[GetAllUsersReq, UpdateReq[User]]]

      val s1: Stream[GetAllUsersReq.Par] = s.map(a.write2read)

      override def listen(
        g: () => M,
        f: M => Unit
      ): Unit = {
        s1.listen({ (x: GetAllUsersReq.Par) =>
          val oldMap: M = g()
          val newMap = setElementToStale(x, oldMap)
          f(newMap)
        })
      }
    }
}
