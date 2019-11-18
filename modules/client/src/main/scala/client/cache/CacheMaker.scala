package client.cache

import client.sodium.core.{Stream, StreamSink}
import dataStorage.stateHolder.UserMap
import dataStorage.{Ref, ReferencedValue, UnTypedRef, Value}
import io.circe.Decoder.Result
import io.circe.{Decoder, Json}
import shapeless.Typeable

import scala.collection.immutable
import scala.reflect.ClassTag

case class CacheMaker[V <: Value[V]](streamSink: StreamSink[UserMap]) {

  var x = "a";

  def getCache(
  )(
    implicit
    decoder:  Decoder[ReferencedValue[V]],
    ct:       ClassTag[V],
    typeable: Typeable[V]
  ): Cache[V] = {

    val setter: Stream[CacheMap[V]] = streamSink.map(makeFrom)

    val updaterFromSetter: Stream[CacheMap[V] => CacheMap[V]] =
      setter.map((y: CacheMap[V]) => { x: CacheMap[V] => y })

    val name: String = UnTypedRef.getName[V]

    val cache = Cache(updaterFromSetter, name)

    println(s"cache is ready: ${cache.cellLoop}")

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
