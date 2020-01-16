package client.cache

import client.cache.commands.UpdateEntityInCacheCmd
import io.circe.generic.JsonCodec
import shapeless.Typeable
import shared.dataStorage._

//import scala.collection.immutable.HashMap
//import io.circe.Decoder.Result
import io.circe._
import io.circe.syntax._
//import io.circe.generic.JsonCodec
import io.circe.generic.auto._

////@JsonCodec
//case class Test[V <: Value[V]](
//  map: Map[Ref[V], TypedReferencedValue[V]]) {
//
//  def toJSON(
//    implicit
//    enc: Encoder[V],
//    encRefHM: Encoder[
//      Map[Ref[V], TypedReferencedValue[V]]
//    ],
//    encRef: Encoder[Ref[V]]
//  ): String = {
//    map.asJson.spaces4
//  }
//
//}
/**
  * @param cacheMap
  * @tparam V
  */

//@JsonCodec
case class CacheMap[V <: Value[V]](
                                    cacheMap: Map[Ref[V], TypedReferencedValue[V]] =
    Map[Ref[V], TypedReferencedValue[V]]()
)(
  implicit
  typeable: Typeable[V]) {

  // https://dzone.com/articles/java-string-format-examples

  def getPrettyPrintedString: String = {
    cacheMap.foldLeft("")(
      (s, v) =>
        s + "value: " + s"${v._2.versionedEntityValue}, "
          .formatted("%40s") +
          s"type: ${v._1.unTypedRef.typeName
            .map(_.s).getOrElse("not-typed error !!!")}, " +
          s"owner: ${v._1.unTypedRef.refToEntityOwningUser.uuid}, " +
          s"uuid: ${v._1.unTypedRef.uuid} " +
          s"version ${v._2.versionedEntityValue.version}" +
          s" \n"
    )
  }

  def getTypeName: String =
    typeable.describe.toString

  def getNumberOfEntries: Int = cacheMap.size

  def resRef(r:Ref[V]): Option[TypedReferencedValue[V]] =cacheMap.get(r)

  def toJSON(
    implicit
    enc: Encoder[V],
    encRefHM: Encoder[
      Map[Ref[V], TypedReferencedValue[V]]
    ],
    encRef: Encoder[Ref[V]]
  ): String = {
//    implicitly[Encoder[V]]
    cacheMap.asJson.spaces4 //fixme
  }
}

object CacheMap {

  def insertReferencedValueTransformer[V <: Value[V]](
    rv: TypedReferencedValue[V]
  )(
    implicit
    typeable: Typeable[V]
  ): CacheMap[V] => CacheMap[V] = { m =>
    {
      val oldMap = m.cacheMap
      val newMap = oldMap + (rv.ref -> rv)
      CacheMap(newMap)
    }
  }

  def updateReferencedValueTransformer[V <: Value[V]](
    updateCommand: UpdateEntityInCacheCmd[V]
  )(
    implicit
    typeable: Typeable[V]
  ): CacheMap[V] => CacheMap[V] = { m =>
    {

      val oldMap = m.cacheMap

      // version check:
      val currentVersion: EntityVersion = oldMap(
        updateCommand.currentTypedReferencedValue.ref
      ).versionedEntityValue.version

      if (currentVersion == updateCommand.currentTypedReferencedValue.versionedEntityValue.version) {
        val versionedValue =
          VersionedValue(updateCommand.newValue, currentVersion.inc)
        val newTypedReferencedValueInCache = TypedReferencedValue(
          versionedValue,
          updateCommand.currentTypedReferencedValue.ref
        )
        val newMap = oldMap + (updateCommand.currentTypedReferencedValue.ref -> newTypedReferencedValueInCache)

        CacheMap(newMap)
      } else CacheMap(oldMap)
    }
  }

}
