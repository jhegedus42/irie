package app.client.ui.caching.cache.comm.write

import app.shared.comm.{PostRequest, WriteRequest}

object WriteRequestHandlerStates {

  type WR = PostRequest[WriteRequest]

  sealed trait WriteHandlerState[Req <: WR] {}

  case class NotCalledYet[Req <: WR]() extends WriteHandlerState[Req]

  case class RequestPending[Req <: WR]()
      extends WriteHandlerState[Req]

  trait RequestArrived[Req <: WR] extends WriteHandlerState[Req]

  case class RequestError[Req   <: WR](errorDescription:String) extends RequestArrived[Req]
  case class RequestSuccess[Req <: WR](par:Req#ParT,res:Req#ResT) extends RequestArrived[Req]

  def toInt[R <: WR](ra: WriteHandlerState[R]): Int = {
    ra match {
      case NotCalledYet()   => 1
      case RequestPending() => 2
      case arrived: RequestArrived[R] =>
        arrived match {
          case RequestError(_)   => 3
          case RequestSuccess(_,_) => 4
          case _                => 5
        }
    }
  }

}
