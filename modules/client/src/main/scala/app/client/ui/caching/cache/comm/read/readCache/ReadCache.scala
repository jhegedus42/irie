package app.client.ui.caching.cache.comm.read.readCache

import app.client.ui.caching.cache.ReadCacheEntryStates.ReadCacheEntryState
import app.client.ui.caching.cache.comm.read.readCache.invalidation.{
  InvalidationTypes,
  Invalidator,
  InvalidatorStream
}
import app.client.ui.caching.cache.comm.write.WriteAjaxReturnedStream.Payload
import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTC
import app.shared.comm.postRequests.read.GetAllUsersReq
import app.shared.comm.postRequests.{
  GetEntityReq,
  GetLatestEntityByIDReq,
  GetUsersNotesReq
}
import app.shared.comm.{PostRequest, ReadRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag

trait ReadCache[Req <: PostRequest[ReadRequest]] {
  private[caching] def getRequestResult( par: Req#ParT ) : ReadCacheEntryState[Req]

  def clearCache(): Unit
}

object ReadCache {


  import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTC._

  implicit val getLatestUserCache = {
    new ReadCacheImpl[GetLatestEntityByIDReq[User]]
  }

  implicit val getLatestNoteCache = {
    new ReadCacheImpl[GetLatestEntityByIDReq[Note]]
  }

  implicit val getUserCache =
    new ReadCacheImpl[GetEntityReq[User]]

  implicit val getAllUsersReqCache = {
    new ReadCacheImpl[GetAllUsersReq]
  }

  implicit val getAllNotesCache = {
    new ReadCacheImpl[GetUsersNotesReq]
  }

  implicit val getNoteCache = new ReadCacheImpl[GetEntityReq[Note]]

}
