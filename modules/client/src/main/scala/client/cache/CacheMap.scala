package client.cache

import dataStorage.{Ref, TypedReferencedValue, Value}

import scala.collection.immutable.HashMap
import io.circe.Decoder.Result
import io.circe._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import shapeless.Typeable

//@JsonCodec
case class Test[V <: Value[V]](
  map: HashMap[Ref[V], TypedReferencedValue[V]]) {

  def toJSON(
    implicit
    enc: Encoder[V],
    encRefHM: Encoder[
      HashMap[Ref[V], TypedReferencedValue[V]]
    ],
    encRef: Encoder[Ref[V]]
  ): String = {
    map.asJson.spaces4
  }

}

//@JsonCodec
case class CacheMap[V <: Value[V]](
  map: HashMap[Ref[V], TypedReferencedValue[V]] =
    HashMap[Ref[V], TypedReferencedValue[V]]()) {

  // https://dzone.com/articles/java-string-format-examples

  def getPrettyPrintedString: String = {
    map.foldLeft("")(
      (s, v) =>
        s + "value: " + s"${v._2.entityValue}, "
          .formatted("%40s") +
          s"type: ${v._1.unTypedRef.typeName
            .map(_.s).getOrElse("not-typed error !!!")}, " +
          s"owner: ${v._1.unTypedRef.refToEntityOwningUser.uuid}, " +
          s"uuid: ${v._1.unTypedRef.uuid} " +
          s" \n"
    )
  }

  def getNumberOfEntries: Int = map.size

//  override def toString: String = {
//
//  }

  def toJSON(
    implicit
    enc: Encoder[V],
    encRefHM: Encoder[
      HashMap[Ref[V], TypedReferencedValue[V]]
    ],
    encRef: Encoder[Ref[V]]
  ): String = {
//    implicitly[Encoder[V]]
    map.asJson.spaces4
  }
}

object CacheMap {

  def insertReferencedValue[V <: Value[V]](
    rv: TypedReferencedValue[V]
  ): CacheMap[V] => CacheMap[V] = { m =>
    {
      val oldMap = m.map
      val newMap = oldMap + (rv.ref -> rv)
      CacheMap(newMap)
    }
  }

}
