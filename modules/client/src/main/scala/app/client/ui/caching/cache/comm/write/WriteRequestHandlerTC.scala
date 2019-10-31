package app.client.ui.caching.cache.comm.write

import WriteRequestHandlerStates.WriteHandlerState
import app.shared.comm.postRequests.{CreateEntityReq, UpdateReq}
import app.shared.comm.{PostRequest, WriteRequest}
import app.shared.entity.entityValue.values.{Note, User}
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag

trait WriteRequestHandlerTC[
  Req <: PostRequest[WriteRequest]] {


  def executeRequest(
    par: Req#ParT
  )(
    implicit
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): WriteHandlerState[Req]
}

object WriteRequestHandlerTC {


  implicit val userUpdater = new WriteRequestHandlerTCImpl[UpdateReq[User]] {}

  implicit val noteUpdater = new WriteRequestHandlerTCImpl[UpdateReq[Note]] {}

  implicit val createUser = new WriteRequestHandlerTCImpl[CreateEntityReq[User]] {}

}
