package app.client.ui.caching.cache.readyOrNot

/**
  *
  *
  * This is a global mutable state.
  * All caches can access this and they should use it
  * to signal their readiness.
  *
  */

object CacheReadyOrNot {

  private var cacheReadiness : Map[CacheID,IsCacheReady] = Map()

  def setIamNotReadyYet(cacheID: CacheID) = ???
  def setIamReady(cacheID: CacheID) = ???


  /**
    *
    * The rendering can only end if every cache is "ready".
    * I.e. they have no pending entries.
    *
    * @return
    */
  def isEverybodyReady : Boolean = ???

}
