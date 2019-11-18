package client.cache

import dataStorage.{Ref, ReferencedValue, Value}

case class CacheMap[V <: Value[V]](
  map: Map[Ref[V], ReferencedValue[V]] = Map[Ref[V], ReferencedValue[V]]()) {

  def getPrettyPrintedString: String = {
    map.foldLeft("")(
      (s, v) =>
        s + s"${v._1.unTypedRef.typeName} ${v._1.unTypedRef.uuid}  ${v._2.entityValue}\n"
    )
  }

  def getNumberOfEntries: Int = map.size

}

object CacheMap {

  def insertReferencedValue[V <: Value[V]](
    rv: ReferencedValue[V]
  ): CacheMap[V] => CacheMap[V] = { m =>
    {
      val oldMap = m.map
      val newMap = oldMap + (rv.ref -> rv)
      CacheMap(newMap)
    }
  }

}
