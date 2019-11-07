package app.shared.comm.viewRequest

import app.shared.comm.viewRequest.ViewRequest.{Parameter, Result}


abstract class ViewRequest {


  type ParT <: Parameter
  type ResT <: Result
}

object ViewRequest {
  trait Result
  trait Parameter
}


