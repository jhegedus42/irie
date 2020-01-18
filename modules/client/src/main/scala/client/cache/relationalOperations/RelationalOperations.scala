package client.cache.relationalOperations

import client.cache.{Cache, CacheMap}
import client.sodium.core.Cell
import shared.dataStorage.{Ref, TypedReferencedValue, Value}
import cats.Functor
import cats.implicits._
import client.cache.relationalOperations.CellOptionMonad.CellOption
import simulacrum._

object RelationalOperations {
  val listOption = List(Some(1), None, Some(2))
  val demo       = Functor[List].compose[Option].map(listOption)(_ + 1)

  implicit val functorForCell: Functor[Cell] = new Functor[Cell] {
    def map[A, B](fa: Cell[A])(f: A => B): Cell[B] = fa.map(f)
  }

  def resolveRef[V <: Value[V]](
    r: CellOption[Ref[V]]
  )(
    implicit
    c: Cache[V]
  ): CellOption[TypedReferencedValue[V]] = {
    new CellOption(Cache.resolveRef(r.co))
  }

//  def resolveCO

//  def resolveListOfOptions[V<:Value[V]]()

  def resolveListOfRefOptions[V <: Value[V]](
    listOfRefOptions: Cell[Option[List[Ref[V]]]]
  ): Cell[Option[List[TypedReferencedValue[V]]]] = {

    // continue here - TOMORROW

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
