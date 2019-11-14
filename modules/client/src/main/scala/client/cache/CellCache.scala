package client.cache

import client.sodium.core._
import client.sodium.core.{Cell, StreamSink}
import dataStorage.stateHolder.UserMap
import dataStorage.{Image, Note, Ref, ReferencedValue, UnTypedRef, User, Value}
import io.circe.Json
import io.circe.generic.JsonCodec
import io.circe.syntax._

import scala.collection.immutable
import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

case class CacheMap[V <: Value[V]](
  map: Map[Ref[V], ReferencedValue[V]] =
    Map[Ref[V], ReferencedValue[V]]()){
}

object CacheMap {
  def makeFrom[V<:Value[V]](um:UserMap)(implicit c:ClassTag[V]) : CacheMap[V] ={

    val name =Value.getName[V]

    def g(t:(UnTypedRef,_)):Boolean = { t._1.typeName.get.s==name.s}

    def h(t:(UnTypedRef, Json)) : (Ref[V],ReferencedValue[V]) = ??? // todo now

    def f(userMap: UserMap):CacheMap[V] = {
      val u = userMap.user
      val l: immutable.Seq[(UnTypedRef, Json)] = userMap.list
      val ln: immutable.Seq[(Ref[V], ReferencedValue[V])] = l.map(h)
      val m: Map[Ref[V], ReferencedValue[V]] = ln.toMap
      CacheMap[V](m)
    }

   f(um)
  }
}


case class Cache[V <: Value[V]](setNewValue:Stream[CacheMap[V]]) {
  val cell=setNewValue.hold(CacheMap())
}

object Cache {

  def makeCache[V <: Value[V]]()(implicit initialValue:Stream[UserMap], ct:ClassTag[V]): Cache[V] = {
    val initValue : Stream[CacheMap[V]] = initialValue.map(CacheMap.makeFrom[V](_))
    Cache(initValue)
  }

}


object NormalizedStateHolder {
  implicit val setNormalizedState= new StreamSink[UserMap]()
  val user:  Cache[User]  = Cache.makeCache[User]()
  val note:  Cache[Note]  = Cache.makeCache[Note]()
  val image: Cache[Image] = Cache.makeCache[Image]()

}

trait CellCacheProvider[P <: Value[P]] {
  def getCellCache: Cache[P]
}

object CellCacheProvider {
  implicit val user = new CellCacheProvider[User] {
    override def getCellCache = NormalizedStateHolder.user
  }
  implicit val note = new CellCacheProvider[Note] {
    override def getCellCache = NormalizedStateHolder.note
  }
  implicit val image = new CellCacheProvider[Image] {
    override def getCellCache = NormalizedStateHolder.image
  }
}
