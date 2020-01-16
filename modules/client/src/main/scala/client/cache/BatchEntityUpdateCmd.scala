package client.cache

import shared.dataStorage.Value

case class BatchEntityUpdateCmd[V <: Value[V]](
  updates: List[UpdateEntitiesInCacheCmd[V]])
