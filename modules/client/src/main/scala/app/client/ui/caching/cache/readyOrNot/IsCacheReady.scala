package app.client.ui.caching.cache.readyOrNot

/**
  *
  * @param cacheID
  * @param readyOrNot is false if the cache has some "Pending" entries.
  */

case class IsCacheReady(cacheID:CacheID,readyOrNot:Boolean)
