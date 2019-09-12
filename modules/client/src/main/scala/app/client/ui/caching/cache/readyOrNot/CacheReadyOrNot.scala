package app.client.ui.caching.cache.readyOrNot

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
