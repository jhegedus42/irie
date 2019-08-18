package app.shared.comm.views

import app.shared.comm.views.PostRequest.{Parameter, Result}

abstract class PostRequest {
  type Par <: Parameter
  type Res <: Result
}

object PostRequest {
  trait Result
  trait Parameter

}





