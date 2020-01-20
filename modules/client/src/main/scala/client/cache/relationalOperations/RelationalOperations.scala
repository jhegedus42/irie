package client.cache.relationalOperations


import client.cache.{Cache, CacheMap}
import client.sodium.core.Cell
import shared.dataStorage.{Ref, TypedReferencedValue, Value}
import cats.Functor
import cats.implicits._
import client.cache.relationalOperations.CellOptionMonad.CellOption
import simulacrum._

object RelationalOperations {

  implicit class Pipe[T](val v: T) extends AnyVal {
    def |>[U] (f: T => U) = f(v)
    // Additional suggestions:
    def $$[U](f: T => U): T = {f(v); v}
    def #!(str: String = ""): T = {println(str + v); v}
  }

  type ResultSet[A <: Value[A]] = Set[TypedReferencedValue[A]]

  type ResultSetVal[A <: Value[A]] = Set[A]

  def toVal[A<:Value[A]] = (rs:ResultSet[A]) =>
    rs.map(_.versionedEntityValue.valueWithoutVersion)

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
    listOfRefOptions: CellOption[List[Ref[V]]]
  )(
    implicit
    c: Cache[V]
  ): CellOption[List[TypedReferencedValue[V]]] = {
    new CellOption(Cache.resolveListOfRefs(listOfRefOptions.co))
  }

  def getAllEntitiesWithFilter[V <: Value[V]](
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

  def coGetAllEntitiesWithFilter[V <: Value[V]](
    filterCriteria: CellOption[TypedReferencedValue[V] => Boolean]
  )(
    implicit
    c: Cache[V]
  ): CellOption[Set[TypedReferencedValue[V]]] = {

    def f(
      cm: CacheMap[V],
      g:  TypedReferencedValue[V] => Boolean
    ): Set[TypedReferencedValue[V]] = {

      val res: Iterable[TypedReferencedValue[V]] =
        cm.cacheMap.values.filter({ x =>
          g(x)
        })

      res.toSet
    }

    val cm = CellOption.fromCell(c.cellLoop)

    val res: CellOption[Set[TypedReferencedValue[V]]] =
      cm.lift2(filterCriteria)(f)
    res
  }

  def getAllAsWhichReferToB[A <: Value[A], B <: Value[B]](
    a:           CellOption[A],
    getRefField: A => Ref[B]
  ): CellOption[Set[TypedReferencedValue[A]]] = ??? // todo-later

}
