package client.cache

import shared.dataStorage.Value

trait CanProvideCache[V <: Value[V]] {
  def getCache: Cache[V]

}

object CanProvideCache {}
