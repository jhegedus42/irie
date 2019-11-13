package client.cache

import client.sodium.core
import client.sodium.core.{Cell, StreamSink}
import dataStorage.{Image, Note, Ref, ReferencedValue, User, Value}
import io.circe.generic.JsonCodec
import io.circe.syntax._

import scala.concurrent.ExecutionContextExecutor

case class CacheMap[V <: Value[V]](
  map: Map[Ref[V], ReferencedValue[V]] =
    Map[Ref[V], ReferencedValue[V]]())

case class Cache[V <: Value[V]](
  s: StreamSink[CacheMap[V]],
  c: Cell[CacheMap[V]]) {}

object Cache {

  def makeCache[V <: Value[V]](): Cache[V] = {
    val s = new StreamSink[CacheMap[V]]()
    val c = s.hold(CacheMap[V]())
    Cache(s, c)
  }
}

object CellCacheStore {
  val user:  Cache[User]  = Cache.makeCache[User]()
  val note:  Cache[Note]  = Cache.makeCache[Note]()
  val image: Cache[Image] = Cache.makeCache[Image]()

}

trait CellCacheProvider[P <: Value[P]] {
  def getCellCache: Cache[P]
}

object CellCacheProvider {
  implicit val user = new CellCacheProvider[User] {
    override def getCellCache = CellCacheStore.user
  }
  implicit val note = new CellCacheProvider[Note] {
    override def getCellCache = CellCacheStore.note
  }
  implicit val image = new CellCacheProvider[Image] {
    override def getCellCache = CellCacheStore.image
  }
}
