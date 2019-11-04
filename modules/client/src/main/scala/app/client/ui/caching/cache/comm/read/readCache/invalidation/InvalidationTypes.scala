package app.client.ui.caching.cache.comm.read.readCache.invalidation

import app.client.ui.caching.cache.ReadCacheEntryStates.ReadCacheEntryState
import app.client.ui.caching.cache.comm.write.WriteAjaxReturnedStream
import app.client.ui.caching.cache.comm.write.WriteAjaxReturnedStream.Payload
import app.shared.comm.{PostRequest, ReadRequest, WriteRequest}

object InvalidationTypes {

  sealed trait InvalidationType
  case class SingleEntry() extends InvalidationType
  case class FullReset() extends InvalidationType

}


