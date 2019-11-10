package syncedNormalizedState.comm

import dataModel.EntityValueType


case class SodiumRouteName[Req<:SodiumCRUDReq[V],V<:EntityValueType[V]](name: String)
