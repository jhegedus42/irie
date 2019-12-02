package client.cache

import dataStorage.{Ref, TypedReferencedValue, Value}

case class CacheMap[V <: Value[V]](
  map: Map[Ref[V], TypedReferencedValue[V]] =
    Map[Ref[V], TypedReferencedValue[V]]()) {

  // https://dzone.com/articles/java-string-format-examples

  def getPrettyPrintedString: String = {
    map.foldLeft("")(
      (s, v) =>
        s + "value: " + s"${v._2.entityValue}, ".formatted("%40s") +
          s"type: ${v._1.unTypedRef.typeName
            .map(_.s).getOrElse("not-typed error !!!")}, " +
          s"owner: ${v._1.unTypedRef.refToEntityOwningUser.uuid}, " +
          s"uuid: ${v._1.unTypedRef.uuid} " +
          s" \n"
    )
  }

  def getNumberOfEntries: Int = map.size

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
