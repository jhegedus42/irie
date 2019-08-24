package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetEntityReq.{
  GetEntityReqPar,
  GetEntityReqRes
}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.{Entity, RefToEntity}

object GetEntityReq {

  case class GetEntityReqPar[V <: EntityValue[V]](refToEntity: RefToEntity[V] ) extends PostRequest.Parameter

  case class GetEntityReqRes[V <: EntityValue[V]](optionEntity: Option[Entity[V]] ) extends PostRequest.Result

}

class GetEntityReq[V <: EntityValue[V]] extends PostRequest {
  override type Par     = GetEntityReqPar[V]
  override type Res     = GetEntityReqRes[V]
  override type PayLoad = V
}
