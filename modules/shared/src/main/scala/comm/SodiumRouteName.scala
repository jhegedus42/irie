package comm

import comm.crudRequests.CRUDReq
import dataStorage.normalizedDataModel.EntityValueType


case class SodiumRouteName[Req<:CRUDReq[V],V<:EntityValueType[V]](name: String)
