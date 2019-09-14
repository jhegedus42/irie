package app.shared.comm.postRequests

import app.shared.comm.PostRequest
import app.shared.comm.postRequests.SumIntRoute.{SumIntPar, SumIntRes}
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

// https://circe.github.io/circe/codecs/semiauto-derivation.html

object SumIntRoute {

  @JsonCodec
  case class SumIntPar(
    x: Int,
    y: Int)
      extends PostRequest.Parameter

  @JsonCodec
  case class SumIntRes(sum: Int) extends PostRequest.Result
}

class SumIntRoute extends PostRequest {
  override type ParT     = SumIntPar
  override type ResT     = SumIntRes
  override type PayLoadT = Unit
}
