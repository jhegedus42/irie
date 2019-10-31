package app.client.ui.components.sodium

import app.client.ui.caching.cache.comm.write.{
  WriteRequestHandlerStates,
  WriteRequestHandlerTC
}
import app.client.ui.caching.cacheInjector.Cache
import app.shared.comm.WriteRequest
import app.shared.comm.postRequests.UpdateReq
import app.shared.comm.postRequests.UpdateReq.UpdateReqPar
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import sodium._

import scala.reflect.ClassTag

case class SodiumWidgeSaveEntityToServer[V <: EntityType[V]](
  stream:    Stream[UpdateReqPar[V]],
  val cache: Cache
)(
  implicit
  c:       WriteRequestHandlerTC[UpdateReq[V]],
  decoder: Decoder[UpdateReq[V]#ResT],
  encoder: Encoder[UpdateReq[V]#ParT],
  ct:      ClassTag[UpdateReq[V]],
  ct2:     ClassTag[UpdateReq[V]#PayLoadT]) {

  def saveEntity(
    par: UpdateReqPar[V]
  ): WriteRequestHandlerStates.WriteHandlerState[UpdateReq[V]] =
    cache.writeToServer[ UpdateReq[V]](par)

  stream.listen((x: UpdateReqPar[V]) => saveEntity(x))

}
