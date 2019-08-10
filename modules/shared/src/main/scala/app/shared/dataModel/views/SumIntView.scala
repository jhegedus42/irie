package app.shared.dataModel.views

import app.shared.comm.views.View
import app.shared.dataModel.views.SumIntView.{SumIntView_Par, SumIntView_Res}

object SumIntView {

    case class SumIntView_Par(x:Int, y:Int ) extends View.Parameter

    case class SumIntView_Res(sum: Int ) extends View.Result



}
class SumIntView extends View {
  type Par = SumIntView_Par
  type Res = SumIntView_Res
}
