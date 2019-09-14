package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetEntityReq.{Par, Res}
import app.shared.entity.entityValue.EntityValue
import app.shared.comm.PostRequest
import app.shared.comm.postRequests.GetEntityReq.{Par, Res}
import app.shared.comm.postRequests.ResetRequest.{Par, Res}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.Entity
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}

import io.circe.generic.JsonCodec, io.circe.syntax._

/**
  *
  * This is a request (HTTP Post) that resets the
  * server's state to a Test State.
  *
  */

object ResetRequest {

  @JsonCodec
  case class Par() extends PostRequest.Parameter

  @JsonCodec
  case class Res(message:String ) extends PostRequest.Result

}

class ResetRequest extends PostRequest {
  override type ParT     = ResetRequest.Par
  override type ResT     = ResetRequest.Res
  override type PayLoadT = String // dummy payload
}
