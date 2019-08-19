package app.shared.dataModel.views

import app.shared.comm.requests.Request
import app.shared.dataModel.views.SumIntPostRequest.{SumIntView_Par, SumIntView_Res}

object SumIntPostRequest {

    case class SumIntView_Par(x:Int, y:Int ) extends Request.PostReqParameter

    case class SumIntView_Res(sum: Int ) extends Request.PostReqResult



}
class SumIntPostRequest extends Request {
  type Par = SumIntView_Par
  type Res = SumIntView_Res
}
