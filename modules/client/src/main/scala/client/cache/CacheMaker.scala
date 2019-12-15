package client.cache

import client.sodium.core.{Stream, StreamSink}
import dataStorage.stateHolder.UserMap
import dataStorage.{
  Ref,
  TypedReferencedValue,
  UnTypedRef,
  Value
}
import io.circe.Decoder.Result
import io.circe.{Decoder, Json}
import shapeless.Typeable

import scala.collection.immutable
import scala.collection.immutable.HashMap
import scala.reflect.ClassTag
import dataStorage.stateHolder.UserMap
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shapeless.Typeable

case class CacheMaker[V <: Value[V]](
  streamSink: StreamSink[UserMap]) {

  var x = "a";

  def getCache(
  )(
    implicit
    decoder:  Decoder[TypedReferencedValue[V]],
    ct:       ClassTag[V],
    typeable: Typeable[V]
  ): Cache[V] = {

    val setter: Stream[CacheMap[V]] =
      streamSink.map(makeFrom)

    val updaterFromSetter
      : Stream[CacheMap[V] => CacheMap[V]] =
      setter.map(
        (y: CacheMap[V]) => { x: CacheMap[V] => y }
      )

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
    d:        Decoder[TypedReferencedValue[V]],
    typeable: Typeable[V]
  ): CacheMap[V] = {

    val name = UnTypedRef.getName[V]

    def g(t: (UnTypedRef, _)): Boolean = {
      t._1.typeName.get.s == name
    }

    def h(
      t: (UnTypedRef, Json)
    ): (Ref[V], TypedReferencedValue[V]) = {
      val ur = t._1

      val json = t._2

      val r = Ref[V](ur)

      val referencedValueV
        : Result[TypedReferencedValue[V]] =
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

      val l: immutable.Seq[(UnTypedRef, Json)] =
        userMap.list

      val lf = l.filter(g)

      val ln
        : immutable.Seq[(Ref[V], TypedReferencedValue[V])] =
        lf.map(h)

      val m: Map[Ref[V], TypedReferencedValue[V]] =
        ln.toMap

      CacheMap[V](m)
    }

    f(um)

  }

}
