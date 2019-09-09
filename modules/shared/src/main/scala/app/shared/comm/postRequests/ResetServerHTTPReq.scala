package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetEntityReq.{GetEntityReqPar, GetEntityReqRes}
import app.shared.entity.entityValue.EntityValue
import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetEntityReq.{GetEntityReqPar, GetEntityReqRes}
import app.shared.comm.postRequests.ResetServerHTTPReq.{Par, Res}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.Entity
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}


object ResetServerHTTPReq {

  case class Par() extends PostRequest.Parameter

  case class Res(message:String ) extends PostRequest.Result

}

class ResetServerHTTPReq extends PostRequest {
  override type Par     = ResetServerHTTPReq.Par
  override type Res     = ResetServerHTTPReq.Res
  override type PayLoad = String // dummy payload
}
