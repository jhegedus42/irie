package app.client.ui.caching.cache.comm.write

import WriteRequestHandlerStates.WriteHandlerState
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.write.WriteAjaxReturnedStream.Payload
import app.shared.comm.postRequests.{CreateEntityReq, UpdateReq}
import app.shared.comm.{PostRequest, WriteRequest}
import app.shared.entity.entityValue.values.{Note, User}
import io.circe.{Decoder, Encoder}
import sodium.StreamSink
import sodium.Stream

import scala.reflect.ClassTag

case class WriteAjaxReturnedStream[Req <: PostRequest[WriteRequest]](
  val s: StreamSink[Payload[Req]] = new StreamSink[Payload[Req]]()){

  def getStream:Stream[Payload[Req]]=s

  def send(p:Payload[Req]): Unit= s.send(p)
}

object WriteAjaxReturnedStream {

  case class Payload[Req <: PostRequest[WriteRequest]](
    par: Req#ParT,
    res: Req#ResT)

}

trait WriteRequestHandlerTC[Req <: PostRequest[WriteRequest]] {

  def executeRequest(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): WriteHandlerState[Req]

  val writeAjaxReturnedStream: WriteAjaxReturnedStream[Req] =
    WriteAjaxReturnedStream[Req]()

}

object WriteRequestHandlerTC {

  implicit val userUpdater =
    new WriteRequestHandlerTCImpl[UpdateReq[User]] {}

  implicit val noteUpdater =
    new WriteRequestHandlerTCImpl[UpdateReq[Note]] {}

  implicit val createUser =
    new WriteRequestHandlerTCImpl[CreateEntityReq[User]] {}

}
