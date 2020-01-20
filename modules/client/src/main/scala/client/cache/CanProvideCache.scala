package client.cache

import shared.dataStorage.model.Value

trait CanProvideCache[V <: Value[V]] {
  def getCache: Cache[V]

}

object CanProvideCache {}
