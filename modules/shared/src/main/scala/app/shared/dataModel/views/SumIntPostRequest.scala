package app.shared.dataModel.views

import app.shared.comm.views.PostRequest
import app.shared.dataModel.views.SumIntPostRequest.{SumIntView_Par, SumIntView_Res}

object SumIntPostRequest {

    case class SumIntView_Par(x:Int, y:Int ) extends PostRequest.Parameter

    case class SumIntView_Res(sum: Int ) extends PostRequest.Result



}
class SumIntPostRequest extends PostRequest {
  type Par = SumIntView_Par
  type Res = SumIntView_Res
}
