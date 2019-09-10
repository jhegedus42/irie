package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetEntityReq.{GetEntityReqPar, GetEntityReqRes}
import app.shared.entity.entityValue.EntityValue
import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetEntityReq.{GetEntityReqPar, GetEntityReqRes}
import app.shared.comm.postRequests.ResetRequest.{Par, Res}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.Entity
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}

/**
  *
  * This is a request (HTTP Post) that resets the
  * server's state to a Test State.
  *
  */

object ResetRequest {

  case class Par() extends PostRequest.Parameter

  case class Res(message:String ) extends PostRequest.Result

}

class ResetRequest extends PostRequest {
  override type Par     = ResetRequest.Par
  override type Res     = ResetRequest.Res
  override type PayLoad = String // dummy payload
}
