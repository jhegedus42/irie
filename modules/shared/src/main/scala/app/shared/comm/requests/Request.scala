package app.shared.comm.requests

import app.shared.comm.requests.Request.{PostReqParameter, PostReqResult}

abstract class Request {
  type Par <: PostReqParameter
  type Res <: PostReqResult
}

object Request {
  trait PostReqResult
  trait PostReqParameter

}





