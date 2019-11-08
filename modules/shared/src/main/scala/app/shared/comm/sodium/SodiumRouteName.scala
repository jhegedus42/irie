package app.shared.comm.sodium

import app.shared.entity.entityValue.EntityType

case class SodiumRouteName[Req<:SodiumCRUDReq[V],V<:EntityType[V]](name: String)
