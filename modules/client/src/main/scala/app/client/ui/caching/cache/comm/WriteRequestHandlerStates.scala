package app.client.ui.caching.cache.comm

import app.shared.comm.{PostRequest, WriteRequest}

object WriteRequestHandlerStates {

  type WR = PostRequest[WriteRequest]

  sealed trait WriteHandlerState[Req <: WR] {}

  case class NotCalledYet[Req <: WR]() extends WriteHandlerState[Req]

  case class RequestPending[Req <: WR]()
      extends WriteHandlerState[Req]

  trait RequestArrived[Req <: WR] extends WriteHandlerState[Req]

  case class RequestError[Req   <: WR]() extends RequestArrived[Req]
  case class RequestSuccess[Req <: WR]() extends RequestArrived[Req]

  def toInt[R <: WR](ra: WriteHandlerState[R]): Int = {
    ra match {
      case NotCalledYet()   => 1
      case RequestPending() => 2
      case arrived: RequestArrived[R] =>
        arrived match {
          case RequestError()   => 3
          case RequestSuccess() => 4
          case _                => 5
        }
    }
  }

}
