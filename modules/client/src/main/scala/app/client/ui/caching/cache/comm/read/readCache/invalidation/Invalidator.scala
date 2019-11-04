package app.client.ui.caching.cache.comm.read.readCache.invalidation

import app.client.ui.caching.cache.ReadCacheEntryStates.ReadCacheEntryState
import app.client.ui.caching.cache.comm.write.WriteAjaxReturnedStream.Payload
import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTC
import app.shared.comm.postRequests.{GetEntityReq, GetLatestEntityByIDReq, UpdateReq}
import app.shared.comm.{PostRequest, ReadRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion

trait Invalidator[Req<:PostRequest[ReadRequest]]{
  type M=Map[Req#ParT, ReadCacheEntryState[Req]]
  def getNewMap(map:M):M
}

object Invalidator {
  implicit def generalInvalidator[Req<:PostRequest[ReadRequest]] : Invalidator[Req] = {
    new Invalidator[Req] {
      override def getNewMap(map: M): M = map
    }
  }
}




