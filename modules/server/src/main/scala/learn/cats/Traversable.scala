package learn.cats

object Traversable {
  import cats.Applicative
  import cats.implicits._
  val list = List(Some(1), Some(2), None)
  val traversed: Option[List[Int]] = list.traverse(identity)


}
