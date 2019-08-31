package app.shared.comm.postRequests

import app.shared.comm.PostRouteType
import app.shared.comm.postRequests.UpdateEntityRoute.{UpdateReqPar, UpdateReqRes}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue

object UpdateEntityRoute {
  case class UpdateReqPar[V <: EntityValue[V]]( entity: Entity[V] ) extends PostRouteType.Parameter

  case class UpdateReqRes[V <: EntityValue[V]]( entity: Entity[V] ) extends PostRouteType.Result
}

class UpdateEntityRoute[V<:EntityValue[V]] extends PostRouteType {
  override type Par = UpdateReqPar[V]
  override type Res = UpdateReqRes[V]
  override type PayLoad = V

}
