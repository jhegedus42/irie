package client.cache

import client.sodium.core._
import client.sodium.core.{Cell, StreamSink}
import dataStorage.stateHolder.UserMap
import dataStorage.{
  Image,
  Note,
  Ref,
  ReferencedValue,
  UnTypedRef,
  User,
  Value
}
import io.circe.{Decoder, Json}
import io.circe.generic.JsonCodec
import io.circe.syntax._
import cats.implicits._
import cats._
import cats.derived._
import io.circe.Decoder.Result
import shapeless.Typeable
import shapeless.syntax.typeable

import scala.collection.immutable
import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

case class CacheMap[V <: Value[V]](
  map: Map[Ref[V], ReferencedValue[V]] =
    Map[Ref[V], ReferencedValue[V]]()) {

  def getPrettyPrintedString: String = {
    map.foldLeft("")(
            (s, v) =>
              s + s"${v._1.unTypedRef.typeName} ${v._1.unTypedRef.uuid}  ${v._2.entityValue}\n"
    )
  }
}

object CacheMap {}

case class Cache[V <: Value[V]](
  cell: (Cell[CacheMap[V]]),
  name: String)

case class CacheMaker[V <: Value[V]](
  streamSink: StreamSink[UserMap]) {

  var x = "a";

  def getCache(
  )(
    implicit
    decoder:  Decoder[ReferencedValue[V]],
    ct:       ClassTag[V],
    typeable: Typeable[V]
  ): Cache[V] = {

    val setCache: Stream[CacheMap[V]] = streamSink.map(makeFrom)

    val name: String = UnTypedRef.getName[V]

    val cell = setCache.hold(CacheMap[V]())

    val cache = Cache(cell, name)

    println(s"cache is ready: ${cache.cell}")

    cache
  }

  def makeFrom(
    um: UserMap
  )(
    implicit
    c:        ClassTag[V],
    d:        Decoder[ReferencedValue[V]],
    typeable: Typeable[V]
  ): CacheMap[V] = {

    val name = UnTypedRef.getName[V]

    def g(t: (UnTypedRef, _)): Boolean = {
      t._1.typeName.get.s == name
    }

    def h(t: (UnTypedRef, Json)): (Ref[V], ReferencedValue[V]) = {
      val ur   = t._1
      val json = t._2
      val r    = Ref[V](ur)
      val referencedValueV: Result[ReferencedValue[V]] =
        d.decodeJson(json)
      val refV = referencedValueV.toOption.get
      // we dare to do this becuase this supposed to be filtered
      // down already to the correct type by g() above
      val res = (r, refV)
      println(s"we are the robots: $res")
      res
    }

    def f(userMap: UserMap): CacheMap[V] = {
      val u = userMap.user
      val l: immutable.Seq[(UnTypedRef, Json)] = userMap.list
      val lf = l.filter(g)
      val ln: immutable.Seq[(Ref[V], ReferencedValue[V])] = lf.map(h)
      val m:  Map[Ref[V], ReferencedValue[V]]             = ln.toMap
      CacheMap[V](m)
    }

    f(um)
  }

}

object NormalizedStateHolder {

  lazy val streamToSetInitialCacheState = new StreamSink[UserMap]()

  val user: Cache[User] =
    CacheMaker(streamToSetInitialCacheState).getCache()

//  val note:  Cache[Note]  = CacheMaker(s).makeCache[Note]()
//  val image: Cache[Image] = CacheMaker(s).makeCache[Image]()

}

trait CellCacheProvider[P <: Value[P]] {
  def getCellCache: Cache[P]
}

object CellCacheProvider {

  implicit val user = new CellCacheProvider[User] {
    override def getCellCache = NormalizedStateHolder.user
  }

//  implicit val note = new CellCacheProvider[Note] {
//    override def getCellCache = NormalizedStateHolder.note
//  }
//
//  implicit val image = new CellCacheProvider[Image] {
//    override def getCellCache = NormalizedStateHolder.image
//  }
}
