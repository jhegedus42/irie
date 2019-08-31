package app.shared.comm

import app.shared.comm.PostRouteType.{Parameter, Result}


abstract class PostRouteType {
  type Par <: Parameter
  type Res <: Result
  type PayLoad
}

object PostRouteType {
  trait Result
  trait Parameter
}





