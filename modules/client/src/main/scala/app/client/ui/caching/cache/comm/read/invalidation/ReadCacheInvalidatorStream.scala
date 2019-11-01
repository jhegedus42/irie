package app.client.ui.caching.cache.comm.read.invalidation

import app.client.ui.caching.cache.comm.AJAXCalls
import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import sodium.StreamSink
import sodium.Stream


case class ReadCacheInvalidatorStream[Req <: PostRequest[ReadRequest]](
  stream: Stream [Req#ParT] )

