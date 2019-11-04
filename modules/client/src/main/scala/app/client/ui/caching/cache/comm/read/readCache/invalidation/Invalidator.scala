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
    f: M  => Unit
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
  implicit def generalInvalidator[
    Req <: PostRequest[ReadRequest]
  ]: Invalidator[Req] = {
    new Invalidator[Req] {
      override def listen(g: () => M, f: M => Unit): Unit =
        {}
    }
  }

  implicit def entityUpdateInvalidator: Invalidator[GetAllUsersReq] =
    new Invalidator[GetAllUsersReq] {

      val w = implicitly[WriteRequestHandlerTC[UpdateReq[User]]]

      val s = w.stream

      val a = implicitly[Adapter[GetAllUsersReq, UpdateReq[User]]]

      val s1: Stream[GetAllUsersReq.Par] =s.map(a.write2read)

      override def listen(g: () => M, f: M => Unit): Unit = {
         s1.listen({
           (x: GetAllUsersReq.Par) =>
             val oldMap: M =g()
             val newMap=setElementToStale(x,oldMap)
             f(newMap)
         }
         )
      }
    }
}
