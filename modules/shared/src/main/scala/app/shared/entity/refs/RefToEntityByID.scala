package app.shared.entity.refs

import app.shared.comm.postRequests.GetLatestEntityByIDReq
import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.entityValue.EntityType
import app.shared.utils.UUID_Utils.EntityIdentity
import monocle.macros.Lenses

@Lenses
case class RefToEntityByID[T <: EntityType[T]](
  entityIdentity: EntityIdentity[T] = EntityIdentity())

object RefToEntityByID {
  implicit def toGetLatestEntityByIDReqPar[V <: EntityType[V]](
    r: RefToEntityByID[V]
  ): GetLatestEntityByIDReq.Par[V] = GetLatestEntityByIDReq.Par[V](r)
}
