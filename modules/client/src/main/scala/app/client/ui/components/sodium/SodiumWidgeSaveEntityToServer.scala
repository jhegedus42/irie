package app.client.ui.components.sodium

import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTC
import app.client.ui.caching.cacheInjector.Cache
import app.shared.comm.WriteRequest
import app.shared.comm.postRequests.UpdateReq
import app.shared.comm.postRequests.UpdateReq.UpdateReqPar
import app.shared.entity.entityValue.EntityType
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}
import sodium._

import scala.reflect.ClassTag

case class SodiumWidgeSaveEntityToServer[V <: EntityType[V]](
  stream:    StreamSink[RefToEntityWithVersion[V]],
  val cache: Cache
)(
  implicit
  c:       WriteRequestHandlerTC[WriteRequest, UpdateReq[V]],
  decoder: Decoder[UpdateReq[V]#ResT],
  encoder: Encoder[UpdateReq[V]#ParT],
  ct:      ClassTag[UpdateReq[V]],
  ct2:     ClassTag[UpdateReq[V]#PayLoadT]) {

  def saveEntity =
    cache.writeToServer[WriteRequest, UpdateReq[V]](???)

  // todo-now CONTINUE HERE

}
