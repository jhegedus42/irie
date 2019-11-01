package app.client.ui.caching.cache.comm.write

import WriteRequestHandlerStates.WriteHandlerState
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.read.invalidation.SReadCacheInvalidator
import app.shared.comm.postRequests.{CreateEntityReq, UpdateReq}
import app.shared.comm.{PostRequest, WriteRequest}
import app.shared.entity.entityValue.values.{Note, User}
import io.circe.{Decoder, Encoder}
import sodium.StreamSink
import sodium.Stream

import scala.reflect.ClassTag

case class WriteAjaxReturnedStream[Req <: PostRequest[WriteRequest]](
  streamSink: StreamSink[(Req#ParT, Req#ResT)] =
    new StreamSink[(Req#ParT, Req#ResT)]()){
  def getStream: Stream[(Req#ParT, Req#ResT)] =streamSink
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
