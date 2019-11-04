package app.client.ui.caching.cache.comm.read.readCache.invalidation

import app.shared.comm.postRequests.read.{AdminPassword, GetAllUsersReq}
import app.shared.comm.postRequests.{CreateEntityReq, GetEntityReq, GetLatestEntityByIDReq, GetUsersNotesReq, UpdateReq}
import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.{RefToEntityByID, RefToEntityWithVersion}
import app.shared.utils.UUID_Utils.EntityIdentity

trait Adapter[
  ReadReq  <: PostRequest[ReadRequest],
  WriteReq <: PostRequest[WriteRequest]] {
  def write2read: WriteReq#ParT => ReadReq#ParT
}

object Adapter {


//  implicit def getUserNotesReqAdapter:Adapter[GetUsersNotesReq,UpdateReq[Note]]=
//    new Adapter[GetUsersNotesReq,UpdateReq[Note]] {
//      override def write2read: UpdateReq.UpdateReqPar[Note] => GetUsersNotesReq.Par =
//        (x: UpdateReq.UpdateReqPar[Note]) => {
////          x.currentEntity.toRef.entityIdentity
//          GetUsersNotesReq.Par()
//        }
//    }

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
        : UpdateReq.UpdateReqPar[User] => GetAllUsersReq.Par = { x =>
        GetAllUsersReq.Par(AdminPassword("titok"))
      }
    }

//  implicit def getAllUsersReqCreateAdapter
//    : Adapter[GetAllUsersReq, CreateEntityReq[User]] = ???

  implicit def getLatestEntityByIDReq[
    V <: EntityType[V]
  ]: Adapter[GetLatestEntityByIDReq[V], UpdateReq[V]] = {
    new Adapter[GetLatestEntityByIDReq[V], UpdateReq[V]] {

      override def write2read
        : UpdateReq.UpdateReqPar[V] => GetLatestEntityByIDReq.Par[V] = {
        (up: UpdateReq.UpdateReqPar[V]) =>
          val ei: EntityIdentity[V] =up.currentEntity.toRef.entityIdentity

          val r: RefToEntityByID[V] =RefToEntityByID[V](ei)
          val le: GetLatestEntityByIDReq.Par[V] =  GetLatestEntityByIDReq.Par[V](r)
          println(s"getLatestEntityByIDReq cache invalidation for :$le")
          le
      }

    }

  }

}
