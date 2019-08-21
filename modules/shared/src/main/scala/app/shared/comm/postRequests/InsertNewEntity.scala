package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.InsertNewEntity.{InsertReqPar, InsertReqRes}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue

object InsertNewEntity {
  case class InsertReqPar[V <: EntityValue[V]](
      entityValue: EntityValue[V]
  ) extends PostRequest.Parameter

  case class InsertReqRes[V <: EntityValue[V]](
      entity: Entity[V]
  ) extends PostRequest.Result
}

class InsertNewEntity[V<:EntityValue[V]] extends PostRequest {
  override type Par = InsertReqPar[V]
  override type Res = InsertReqRes[V]
  override type PayLoad = V

}