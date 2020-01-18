package client.cache.relationalOperations

import client.cache.{Cache, CacheMap}
import client.sodium.core.Cell
import shared.dataStorage.{Ref, TypedReferencedValue, Value}
import cats.Functor
import cats.implicits._
import simulacrum._

object RelationalOperations {
  val listOption = List(Some(1), None, Some(2))
  val demo       = Functor[List].compose[Option].map(listOption)(_ + 1)

  implicit val functorForCell: Functor[Cell] = new Functor[Cell] {
    def map[A, B](fa: Cell[A])(f: A => B): Cell[B] = fa.map(f)
  }

  def resolveRef[V <: Value[V]](
    r: Cell[Option[Ref[V]]]
  )(
    implicit
    c: Cache[V]
  ): Cell[Option[TypedReferencedValue[V]]] = {
    Cache.resolveRef(r)
  }

//  def resolveListOfOptions[V<:Value[V]]()

  def resolveListOfRefOptions[V <: Value[V]](
    listOfRefOptions: Cell[List[Option[Ref[V]]]]
  ):Cell[Option[List[TypedReferencedValue[V]]]] = {

    ???
  }

  case class CellOption[V](co: Cell[Option[V]])
  // todo write monad for cellOption (classical, "scala" monad)

//  def resolveList

//  trait Resolveable[V<:Value[V]]  {
//    def resolve(x: Cell[Ref[V]]): Cell[V]
//  }

//  implicit def resolveable[V<:Value[V]:Cache]: Resolveable[V] = new Resolveable[V] {

//    override def resolve(x: Cell[Ref[V]]) :Cell[TypedReferencedValue[V]] = resolve(x)
//    override def resolve(x: Cell[Ref[V]]): Cell[TypedReferencedValue[V]] = resolveCell(x)
//  }

//  case class Cached[V](v:V)

//  object Cached {
//    implicit val functorForCached: Functor[Cached] = new Functor[Cached] {
//      def map[A, B](fa: Cached[A])(f: A => B): Cached[B] = fa.copy(v=f(fa.v))
//    }

//  }

//  implicit def resolveableForRef[V<:Value[V]] = new Resolveable[Ref[_<:V]] {
  //      override def resolve(x: Cell[Ref[V]]): Cell[TypedReferencedValue[V]] = ???
//          override def resolve(x: Cell[Ref[V]]): Cell[V] = ???
//    override def resolve(x: Cell[Ref[_ <: V][V]]): Cell[V] = ???
//  }

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
