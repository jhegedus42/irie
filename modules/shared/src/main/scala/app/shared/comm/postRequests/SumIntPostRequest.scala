package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.SumIntPostRequest.{SumIntPar, SumIntRes}

object SumIntPostRequest {

    case class SumIntPar(x:Int, y:Int ) extends PostRequest.Parameter

    case class SumIntRes(sum: Int ) extends PostRequest.Result
}


class SumIntPostRequest extends PostRequest {
  override type Par = SumIntPar
  override type Res = SumIntRes
  override type PayLoad = Unit
}






