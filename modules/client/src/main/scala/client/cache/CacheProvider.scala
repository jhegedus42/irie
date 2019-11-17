package client.cache

import client.sodium.core.StreamSink
import dataStorage.{User, Value}
import dataStorage.stateHolder.UserMap

object CacheProvider {}

import client.sodium.core.Cell
import dataStorage.Value

case class Cache[V <: Value[V]](
  cell: (Cell[CacheMap[V]]),
  name: String) {}

object Cache {
  lazy val streamToSetInitialCacheState = new StreamSink[UserMap]()

  implicit val user: Cache[User] =
    CacheMaker(streamToSetInitialCacheState).getCache()

  //  val note:  Cache[Note]  = CacheMaker(s).makeCache[Note]()
  //  val image: Cache[Image] = CacheMaker(s).makeCache[Image]()

}
