package app.shared.comm.views

import app.shared.comm.views.View.{Parameter, Result}

abstract class View {
  type Par <: Parameter
  type Res <: Result
}

object View {
  trait Result
  trait Parameter

}





