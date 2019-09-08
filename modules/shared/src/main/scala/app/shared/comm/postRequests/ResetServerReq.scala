package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetEntityReq.{GetEntityReqPar, GetEntityReqRes}
import app.shared.entity.entityValue.EntityValue
import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetEntityReq.{GetEntityReqPar, GetEntityReqRes}
import app.shared.comm.postRequests.ResetServerReq.{ResetServerReqPar, ResetServerReqRes}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.Entity
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}


object ResetServerReq {

  case class ResetServerReqPar() extends PostRequest.Parameter

  case class ResetServerReqRes( numberOfEntries: Int ) extends PostRequest.Result

}

class ResetServerReq extends PostRequest {
  override type Par     = ResetServerReqPar
  override type Res     = ResetServerReqRes
//  override type PayLoad = V
}
