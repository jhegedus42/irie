package app.shared.comm

import app.shared.comm.PostRequest.{Parameter, Result}


abstract class PostRequest {
  type ParT <: Parameter
  type ResT <: Result
  type PayLoadT
}

object PostRequest {
  trait Result
  trait Parameter
}





