package app.shared.comm.postRequests

import app.shared.comm.PostRouteType
import app.shared.comm.postRequests.GetEntityRoute.{
  GetEntityReqPar,
  GetEntityReqRes
}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.Entity
import app.shared.entity.refs.{
  RefToEntityWithVersion,
  RefToEntityWithoutVersion
}

object GetEntityRoute {

  case class GetEntityReqPar[V <: EntityValue[V]](
    refToEntityWithoutVersion: RefToEntityWithoutVersion[V])
      extends PostRouteType.Parameter

  case class GetEntityReqRes[V <: EntityValue[V]](
    optionEntity: Option[Entity[V]])
      extends PostRouteType.Result

}

class GetEntityRoute[V <: EntityValue[V]]
    extends PostRouteType {
  override type Par     = GetEntityReqPar[V]
  override type Res     = GetEntityReqRes[V]
  override type PayLoad = V
}
