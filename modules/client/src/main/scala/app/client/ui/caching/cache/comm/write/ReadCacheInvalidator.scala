package app.client.ui.caching.cache.comm.write

import app.shared.comm.{PostRequest, WriteRequest}

trait ReadCacheInvalidator[RT<:WriteRequest,Req<:PostRequest[RT]]{
  def invalidateReadCache():Unit
}
