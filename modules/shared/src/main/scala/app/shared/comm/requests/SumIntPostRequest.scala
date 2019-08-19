package app.shared.comm.requests

import app.shared.comm.PostRequest
import app.shared.comm.requests.SumIntPostRequest.{SumIntPar, SumIntRes}

object SumIntPostRequest {

    case class SumIntPar(x:Int, y:Int ) extends PostRequest.Parameter

    case class SumIntRes(sum: Int ) extends PostRequest.Result
}


class SumIntPostRequest extends PostRequest {
  type Par = SumIntPar
  type Res = SumIntRes
}






