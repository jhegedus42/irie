package sodiumComm

import refs.entityValue.EntityType

case class SodiumRouteName[Req<:SodiumCRUDReq[V],V<:EntityType[V]](name: String)
