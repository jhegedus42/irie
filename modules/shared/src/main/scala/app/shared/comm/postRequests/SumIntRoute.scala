package app.shared.comm.postRequests

import app.shared.comm.PostRouteType
import app.shared.comm.postRequests.SumIntRoute.{SumIntPar, SumIntRes}

object SumIntRoute {

    case class SumIntPar(x:Int, y:Int ) extends PostRouteType.Parameter

    case class SumIntRes(sum: Int ) extends PostRouteType.Result
}


class SumIntRoute extends PostRouteType {
  override type Par = SumIntPar
  override type Res = SumIntRes
  override type PayLoad = Unit
}






