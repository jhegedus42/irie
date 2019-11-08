package app.client.ui.caching.cache.comm.write

import app.shared.comm.{PostRequest, WriteRequest}

object WriteRequestHandlerStates {

  type WR = PostRequest[WriteRequest]

  sealed trait WriteHandlerState[Req <: WR] {
    def getRes: Option[Req#ResT]
  }

  case class NotCalledYet[Req <: WR]()
      extends WriteHandlerState[Req] {
    override def getRes = None
  }

  case class RequestPending[Req <: WR](par: Req#ParT)
      extends WriteHandlerState[Req] {
    override def getRes: Option[Req#ResT] = None
  }

  trait RequestArrived[Req <: WR] extends WriteHandlerState[Req]

  case class RequestError[Req <: WR](errorDescription: String)
      extends RequestArrived[Req] {
    override def getRes: Option[Req#ResT] = None
  }

  case class RequestSuccess[Req <: WR](
    par: Req#ParT,
    res: Req#ResT)
      extends RequestArrived[Req] {
    override def getRes: Option[Req#ResT] = Some(res)
  }

  def toInt[R <: WR](ra: WriteHandlerState[R]): Int = {
    ra match {
      case NotCalledYet()    => 1
      case RequestPending(_) => 2
      case arrived: RequestArrived[R] =>
        arrived match {
          case RequestError(_)      => 3
          case RequestSuccess(_, _) => 4
          case _                    => 5
        }
    }
  }

}
