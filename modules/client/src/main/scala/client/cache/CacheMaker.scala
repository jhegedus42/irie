package client.cache

import client.sodium.core.{Stream, StreamSink}
import io.circe.Decoder.Result
import io.circe.{Decoder, Json}
import shapeless.Typeable

import scala.collection.immutable
import scala.collection.immutable.HashMap
import scala.reflect.ClassTag
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shapeless.Typeable
import shared.dataStorage.{
  Ref,
  TypedReferencedValue,
  UnTypedRef,
  UnTypedReferencedValue,
  Value
}
import shared.dataStorage.stateHolder.UserMap

case class CacheMaker[V <: Value[V]: Encoder](
  streamSink: StreamSink[UserMap]
)(
  implicit
  encoder: Encoder[TypedReferencedValue[V]]) {

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

    val updaterFromSetter: Stream[CacheMap[V] => CacheMap[V]] =
      setter.map(
        (y: CacheMap[V]) => { x: CacheMap[V] => y }
      )

    val name: String = UnTypedRef.getTypeNameAsString[V]

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

    val typeName: String = UnTypedRef.getTypeNameAsString[V]

    def g(t: UnTypedReferencedValue): Boolean = {
      t.unTypedRef.typeName.get.s == typeName
    }

    def h(
      t: UnTypedReferencedValue
    ): (Ref[V], TypedReferencedValue[V]) = {
      val ur: UnTypedRef = t.unTypedRef

      val json: Json = t.value.value.json

      val r = shared.dataStorage.Ref[V](ur)

      val referencedValueV: Result[TypedReferencedValue[V]] =
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

      val l: immutable.Seq[UnTypedReferencedValue] =
        userMap.list

      val lf: immutable.Seq[UnTypedReferencedValue] = l.filter(g)

      val ln: immutable.Seq[(Ref[V], TypedReferencedValue[V])] =
        lf.map(h)

      val m: Map[Ref[V], TypedReferencedValue[V]] =
        ln.toMap

      CacheMap[V](m)
    }

    f(um)

  }

}
