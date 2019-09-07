package app.shared.comm

import app.shared.comm.PostRequest.{Parameter, Result}


abstract class PostRequest {
  type Par <: Parameter
  type Res <: Result
  type PayLoad
}

object PostRequest {
  trait Result
  trait Parameter
}





