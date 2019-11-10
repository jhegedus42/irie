package syncedNormalizedState.comm

import refs.EntityType


case class SodiumRouteName[Req<:SodiumCRUDReq[V],V<:EntityType[V]](name: String)
