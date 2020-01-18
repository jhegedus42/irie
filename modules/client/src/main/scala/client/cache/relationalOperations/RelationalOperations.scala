package client.cache.relationalOperations

import client.cache.{Cache, CacheMap}
import client.sodium.core.Cell
import shared.dataStorage.{Ref, TypedReferencedValue, Value}

object RelationalOperations {

  def resolve[V <: Value[V]](
    r: Cell[Ref[V]]
  )(
    implicit
    c: Cache[V]
  ): Cell[TypedReferencedValue[V]] = {

    ???
  }

  def filterTable[V <: Value[V]](
    filterCriteriaCell: Cell[V => Boolean]
  )(
    implicit
    c: Cache[V]
  ): Cell[Set[TypedReferencedValue[V]]] = {

    def f(
      cm: CacheMap[V],
      g:  V => Boolean
    ): Set[TypedReferencedValue[V]] = {

      val res: Iterable[TypedReferencedValue[V]] =
        cm.cacheMap.values.filter({ x =>
          g(x.versionedEntityValue.valueWithoutVersion)
        })

      res.toSet
    }

    c.cellLoop.lift(filterCriteriaCell, f)

  }

}
