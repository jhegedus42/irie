package app.shared.comm.postRequests

import app.shared.comm.PostRouteType
import app.shared.comm.postRequests.InsertNewEntityRoute.{InsertReqPar, InsertReqRes}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue

object InsertNewEntityRoute {
  case class InsertReqPar[V <: EntityValue[V]]( value: V ) extends PostRouteType.Parameter

  case class InsertReqRes[V <: EntityValue[V]]( entity: Entity[V] ) extends PostRouteType.Result
}

class InsertNewEntityRoute[V<:EntityValue[V]] extends PostRouteType {
  override type Par = InsertReqPar[V]
  override type Res = InsertReqRes[V]
  override type PayLoad = V

}