package app.client.ui.caching.cache.comm.read.readCache.invalidation

import app.shared.comm.postRequests.read.{AdminPassword, GetAllUsersReq}
import app.shared.comm.postRequests.{CreateEntityReq, GetEntityReq, UpdateReq}
import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion

trait Adapter[
  ReadReq  <: PostRequest[ReadRequest],
  WriteReq <: PostRequest[WriteRequest]] {
  def write2read: WriteReq#ParT => ReadReq#ParT
}

object Adapter {
  implicit def entityUpdateAdapter[
    V <: EntityType[V]
  ]: Adapter[GetEntityReq[V], UpdateReq[V]] =
    new Adapter[GetEntityReq[V], UpdateReq[V]] {
      override def write2read
        : UpdateReq.UpdateReqPar[V] => GetEntityReq.Par[V] = {
        par: UpdateReq.UpdateReqPar[V] =>
          {
            val p = par.currentEntity.toRef
            val x: GetEntityReq.Par[V] = GetEntityReq.Par(p)
            x
          }

      }
    }

  implicit def getAllUsersReqUpdateAdapter
    : Adapter[GetAllUsersReq, UpdateReq[User]] =
    new Adapter[GetAllUsersReq, UpdateReq[User]] {
      override def write2read
        : UpdateReq.UpdateReqPar[User] => GetAllUsersReq.Par = {
        x=> GetAllUsersReq.Par(AdminPassword("titok"))
      }
    }

  implicit def getAllUsersReqCreateAdapter
    : Adapter[GetAllUsersReq, CreateEntityReq[User]] = ???

}
