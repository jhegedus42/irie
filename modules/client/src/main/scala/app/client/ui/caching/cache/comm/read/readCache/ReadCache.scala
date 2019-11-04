package app.client.ui.caching.cache.comm.read.readCache

import app.client.ui.caching.cache.ReadCacheEntryStates.ReadCacheEntryState
import app.client.ui.caching.cache.comm.read.readCache.invalidation.{
  Helper,
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
  GetUsersNotesReq,
  UpdateReq
}
import app.shared.comm.{PostRequest, ReadRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag

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

  def clearCache(): Unit
}

object ReadCache {


  import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTC._

  implicit val getLatestUserCache = {

    new ReadCacheImpl[GetLatestEntityByIDReq[User]](
      Helper.getLatestEntityByIDReqInvalidator
    )
  }

  implicit val getUserCache =
    new ReadCacheImpl[GetEntityReq[User]](
      Helper.getEntityReqInvaliator
    )

  implicit val getAllUsersReqCache = {



//    implicit val x = None

    //todo-now continue here
    // create user triggers this
    // create a clear cache on any user change
    // use Full Reset


    new ReadCacheImpl[GetAllUsersReq](None)
  }

  implicit val getAllNotesCache = {
    new ReadCacheImpl[GetUsersNotesReq](None)
  }

  implicit val getNoteCache =
    new ReadCacheImpl[GetEntityReq[Note]](
      Helper.getEntityReqInvaliator
    )

}
