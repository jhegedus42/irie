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
  incomingInitialUserMapStream: StreamSink[UserMap]
)(
  implicit
  encoder: Encoder[TypedReferencedValue[V]]) {

  var x = "a";

  def getCache(
  )(
    implicit
    decoder:  Decoder[V],
    ct:       ClassTag[V],
    typeable: Typeable[V]
  ): Cache[V] = {

    lazy val cacheMapInitializerStream: Stream[CacheMap[V]] =
      incomingInitialUserMapStream.map(userMap2CacheMap)

    lazy val cacheMapInitializerTransformerStream
      : Stream[CacheMap[V] => CacheMap[V]] =
      cacheMapInitializerStream.map(
        (y: CacheMap[V]) => { x: CacheMap[V] => y }
      )

    lazy val typeName: String = UnTypedRef.getTypeNameAsString[V]

    val cache = Cache(cacheMapInitializerTransformerStream, typeName)

    println(s"cache is ready: ${cache.cellLoop}")

    cache
  }

  def userMap2CacheMap(
    um: UserMap
  )(
    implicit
    c:        ClassTag[V],
    d:        Decoder[V],
    typeable: Typeable[V]
  ): CacheMap[V] = {

    val typeName: String = UnTypedRef.getTypeNameAsString[V]

    def g(t: UnTypedReferencedValue): Boolean = {
      t.unTypedRef.typeName.get.s == typeName
    }

    def h(
      t: UnTypedReferencedValue
    ): (Ref[V], TypedReferencedValue[V]) = {
      lazy val ur: UnTypedRef = t.unTypedRef

      lazy val r: Ref[V] = shared.dataStorage.Ref[V](ur)

      lazy val trv: Option[TypedReferencedValue[V]] =
        UnTypedReferencedValue.toTypedReferencedValue[V](t)

      lazy val res = (r, trv.get)
      // we dare to do this becuase this supposed to be filtered
      // down already to the correct type by g() above

      println(s"we are the robots: $res")
      res
    }

    def f(userMap: UserMap): CacheMap[V] = {

      lazy val l: immutable.Seq[UnTypedReferencedValue] =
        userMap.list

      lazy val lf: immutable.Seq[UnTypedReferencedValue] = l.filter(g)

      lazy val ln: immutable.Seq[(Ref[V], TypedReferencedValue[V])] =
        lf.map(h)

      lazy val m: Map[Ref[V], TypedReferencedValue[V]] =
        ln.toMap

      CacheMap[V](m)
    }

    f(um)

  }

}
