package syncedNormalizedState.comm

import entity.EntityType


case class SodiumRouteName[Req<:SodiumCRUDReq[V],V<:EntityType[V]](name: String)
