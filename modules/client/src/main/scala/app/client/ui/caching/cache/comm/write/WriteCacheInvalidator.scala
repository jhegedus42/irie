package app.client.ui.caching.cache.comm.write

import app.shared.comm.{PostRequest, WriteRequest}

trait WriteCacheInvalidator[RT<:WriteRequest,Req<:PostRequest[RT]]{
  def invalidateReadCache():Unit
}
