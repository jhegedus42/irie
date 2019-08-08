package app.shared.views

abstract class View {
  type Par <: Parameter
  type Res <: Result
}

trait Result
trait Parameter




object View1_HolderObject {

  case class View1_Par(s: String ) extends Parameter // View1-t parameterkent ide a Parameter[...-be]

  case class View1_Res(res: String ) extends Result

  class View1 extends View {
    type Par = View1_Par
    type Res = View1_Res
  }
}

object View2_HolderObject {

  case class View2_Par(i: Int) extends Parameter

  case class View2_Res(res: Int) extends Result

  class View2 extends View {
    type Par = View2_Par
    type Res = View2_Res
  }
}
