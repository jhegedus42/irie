package app.client.ui.caching.cache.comm.read.invalidation

import app.client.ui.caching.cache.comm.AJAXCalls
import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import sodium.StreamSink
import sodium.Stream

abstract class ReadCacheInvalidator[Req <: PostRequest[ReadRequest]] {

  def getStream : Stream[Req#ParT]
}

case class SReadCacheInvalidator[Req <: PostRequest[ReadRequest]](
  streamSink: StreamSink [Req#ParT] ) extends ReadCacheInvalidator[Req] {
  override def getStream= streamSink
}
