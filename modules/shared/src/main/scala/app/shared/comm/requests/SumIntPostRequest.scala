package app.shared.comm.requests

import app.shared.comm.Request
import app.shared.comm.requests.SumIntPostRequest.{SumIntView_Par, SumIntView_Res}

object SumIntPostRequest {

    case class SumIntView_Par(x:Int, y:Int ) extends Request.PostReqParameter

    case class SumIntView_Res(sum: Int ) extends Request.PostReqResult
}


class SumIntPostRequest extends Request {
  type Par = SumIntView_Par
  type Res = SumIntView_Res
}






