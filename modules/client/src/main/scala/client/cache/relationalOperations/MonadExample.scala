package client.cache.relationalOperations

object MonadExample {
  val no = NoInt
  val maybe = SomeInt(1)
//  for {
//    a <- maybe
//    b <- no
////    c = a*b
//  } yield a + c
}

sealed trait MaybeInt {
  def map(f: Int => Int): MaybeInt
  def flatMap(f: Int => MaybeInt): MaybeInt
}

case class SomeInt(i: Int) extends MaybeInt {
  def map(f: Int => Int): MaybeInt = SomeInt(f(i))
  def flatMap(f: Int => MaybeInt): MaybeInt = f(i)
}

case object NoInt extends MaybeInt {
  def map(f: Int => Int): MaybeInt = NoInt
  def flatMap(f: Int => MaybeInt): MaybeInt = NoInt
}

