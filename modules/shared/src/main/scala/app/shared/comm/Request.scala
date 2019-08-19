package app.shared.comm

import app.shared.comm.Request.{PostReqParameter, PostReqResult}


abstract class Request {
  type Par <: PostReqParameter
  type Res <: PostReqResult
}

object Request {
  trait PostReqResult
  trait PostReqParameter

}





