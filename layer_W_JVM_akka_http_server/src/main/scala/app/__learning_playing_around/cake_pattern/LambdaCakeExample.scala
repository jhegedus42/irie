package app.__learning_playing_around.cake_pattern

object LambdaInheritExample extends App{

  trait Lambda {
    println("Lambda init - Line 6")
    lazy val l = "Lambda"
  }


  trait Lambda2 extends Lambda{
    println("Lambda2 init Line 12")
    lazy override val l: String = "20"
  }

  trait Calculus extends Lambda{
    println("Calculus init - Line 17")
    lazy val c = "Calculus"
    lazy val lc: String = s"$l $c"
  }

  case class CalculusImpl() extends Calculus with Lambda2 {
    println (s"lc = ${lc}")
  }

  val ci= CalculusImpl()

  println (ci.l)
  println (ci.l)


}

object LambdaCakeExample {

  trait Lambda {
    val l = "Lambda"
  }

  trait Calculus {
    this: Lambda =>
    val c = "Calculus"
    val lc: String = l + c
  }

}
