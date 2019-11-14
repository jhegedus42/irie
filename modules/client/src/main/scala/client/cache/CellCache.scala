package client.cache

import client.sodium.core._
import client.sodium.core.{Cell, StreamSink}
import dataStorage.stateHolder.UserMap
import dataStorage.{Image, Note, Ref, ReferencedValue, UnTypedRef, User, Value}
import io.circe.{Decoder, Json}
import io.circe.generic.JsonCodec
import io.circe.syntax._
import cats.implicits._
import cats._
import cats.derived._
import io.circe.Decoder.Result

import scala.collection.immutable
import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

case class CacheMap[V <: Value[V]](
  map: Map[Ref[V], ReferencedValue[V]] =
    Map[Ref[V], ReferencedValue[V]]()){
}

object CacheMap {
  def makeFrom[V<:Value[V]](um:UserMap)(implicit c:ClassTag[V], d:Decoder[ReferencedValue[V]]) : CacheMap[V] ={

    val name =Value.getName[V]

    def g(t:(UnTypedRef,_)):Boolean = { t._1.typeName.get.s==name.s}

    def h(t:(UnTypedRef, Json)) : (Ref[V],ReferencedValue[V]) = {
      val ur=t._1
      val json=t._2
      val r= Ref[V](ur)
      val referencedValueV: Result[ReferencedValue[V]] =d.decodeJson(json)
      val refV=referencedValueV.toOption.get
      // we dare to do this becuase this supposed to be filtered
      // down already to the correct type by g() above
      (r,refV)
    }

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


case class Cache[V <: Value[V]](setNewValue:Stream[CacheMap[V]],name:String) {
  val cell=setNewValue.hold(CacheMap())
}

object Cache {

  def makeCache[V <: Value[V]]()(implicit initialValue:Stream[UserMap], decoder: Decoder[ReferencedValue[V]], ct:ClassTag[V]): Cache[V] = {
    val initValue : Stream[CacheMap[V]] = initialValue.map(CacheMap.makeFrom[V](_))
    val name=Value.getName[V].s
    val c=Cache(initValue,name)
    println(s"cache is ready: $c")
    c
  }
}


object NormalizedStateHolder {
  implicit lazy val setNormalizedState= new StreamSink[UserMap]()
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
