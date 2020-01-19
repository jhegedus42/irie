package client.cache.relationalOperations

import client.sodium.core.Cell

// todo later https://www.geekabyte.io/2018/07/rolling-your-own-monad-to-deal-with.html

object CellOptionMonad {

//  sealed trait CellOptionMonad[A] {
//    def map[B](f: A => B): CellOptionMonad[B]
//    def flatMap()
//  }

  case class CellOption[A](co: Cell[Option[A]]) {

    def map[B](f: A => B): CellOption[B] = {
      new CellOption(co.map(_.map(f)))
    }

    def flatMap[B](f: A => CellOption[B]): CellOption[B] = {
      val g: A => Cell[Option[B]] = (a: A) => f(a).co

      val res1: Cell[Option[Cell[Option[B]]]] = co.map(_.map(g))
      val res2: Cell[Cell[Option[B]]] =
        res1.map(_.getOrElse(new Cell(None)))

      val res3: Cell[Option[B]] = Cell.switchC(res2)
      new CellOption[B](res3)
    }

    def lift2[B, C](
      other: CellOption[B]
    )(f:     (A, B) => C
    ): CellOption[C] = {

      val res2 = co.map({ oa: Option[A] =>
        {
          val co2: Cell[Option[C]] = other.co.map({ ob: Option[B] =>
            val res1: Option[C] = for {
              a <- oa
              b <- ob

            } yield f(a, b)
            res1
          })
          co2
        }
      })

      val r = Cell.switchC(res2)

      CellOption.fromCellOption(r)

    }

  }

//    def optMap[B](f: A => Option[B]): CellOption[B] = {
//      val c1: Cell[Option[B]] = co.map(_.map(f).flatten)
//      new CellOption[B](c1)
//    }

//  }

  object CellOption {

    def apply[A](a: A): CellOption[A] = {
      val some: Option[A]       = Some(a)
      val c:    Cell[Option[A]] = new Cell(some)
      new CellOption[A](c)
    }

    def fromCell[A](a: Cell[A]): CellOption[A] = {
      val c: Cell[Option[A]] = a.map(Some(_))
      new CellOption[A](c)
    }

    def fromCellOption[A](co: Cell[Option[A]]): CellOption[A] = {
      new CellOption[A](co)
    }

    def flattenOpt[A](co: CellOption[Option[A]]): CellOption[A] = {
      new CellOption(co.co.map(_.flatten))
    }

  }

  lazy val testFor = for {
    a <- CellOption(1)
    b <- CellOption(2)
  } yield (a + b)

  println(testFor)

  def lift2CO[A, B, C](
    a: Cell[Option[A]],
    b: Cell[Option[B]],
    f: (A, B) => Option[C]
  ) = {
//    a.lift()
    ???
  }

  def optMap2CO[A, B, C](
    a: Cell[Option[A]],
    b: Cell[Option[B]],
    f: (A, B) => Option[C]
  ): Cell[Option[C]] = {

    ???
  }

  def mapCO[A, B](
    coa: Cell[Option[B]],
    f:   A => B
  ): Cell[Option[B]] = ???

}

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
