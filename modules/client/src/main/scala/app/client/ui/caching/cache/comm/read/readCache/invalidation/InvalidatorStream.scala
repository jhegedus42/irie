package app.client.ui.caching.cache.comm.read.readCache.invalidation

import app.client.ui.caching.cache.comm.AJAXCalls
import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}
import sodium.StreamSink
import sodium.Stream


case class InvalidatorStream[Req <: PostRequest[ReadRequest]](
  stream: Stream [Req#ParT] )

