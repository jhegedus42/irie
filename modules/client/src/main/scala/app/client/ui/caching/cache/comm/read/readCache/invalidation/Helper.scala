package app.client.ui.caching.cache.comm.read.readCache.invalidation

import app.client.ui.caching.cache.comm.write.{WriteAjaxReturnedStream, WriteRequestHandlerTC}
import app.client.ui.caching.cache.comm.write.WriteAjaxReturnedStream.Payload
import app.shared.comm.postRequests.{GetEntityReq, GetLatestEntityByIDReq, UpdateReq}
import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion

object Helper {



  def getInvalidator[
    WriteReq <: PostRequest[WriteRequest],
    ReadReq  <: PostRequest[ReadRequest]
  ](ws: WriteAjaxReturnedStream[WriteReq],
    f:  Payload[WriteReq] => ReadReq#ParT
  ): Option[InvalidatorStream[ReadReq]] =
    Some(new InvalidatorStream[ReadReq](ws.getStream.map(f)))

  def getEntityReqInvaliator[V <: EntityType[V]](
    implicit wrh: WriteRequestHandlerTC[UpdateReq[V]]
  ): Option[InvalidatorStream[GetEntityReq[V]]] = {
    def f: Payload[UpdateReq[V]] => GetEntityReq[V]#ParT = {
      ur: Payload[UpdateReq[V]] =>
        val r1: RefToEntityWithVersion[V] = ur.par.currentEntity.toRef
        GetEntityReq.Par[V](r1)
    }
    Helper.getInvalidator[UpdateReq[V], GetEntityReq[V]](
      wrh.writeAjaxReturnedStream,
      f
    )
  }

  val getLatestEntityByIDReqInvalidator: Option[InvalidatorStream[GetLatestEntityByIDReq[User]]] = {
    val f = { p: Payload[UpdateReq[User]] =>
      GetLatestEntityByIDReq.Par[User](
        p.par.currentEntity.toRef.entityIdentity
      )
    }

    Some(
      InvalidatorStream[GetLatestEntityByIDReq[User]](
        WriteRequestHandlerTC.userUpdater.writeAjaxReturnedStream.getStream
          .map(f)
      )
    )
  }
}
