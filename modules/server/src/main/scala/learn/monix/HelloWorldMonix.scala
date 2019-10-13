package learn.monix

import monix.execution.CancelableFuture

object HelloWorldMonix extends  App{

  // todo later - https://monix.io/docs/3x/intro/hello-world.html

  // We need a scheduler whenever asynchronous
  // execution happens, substituting your ExecutionContext
  import monix.execution.Scheduler.Implicits.global

  // Needed below
  import scala.concurrent.Await
  import scala.concurrent.duration._

  import monix.eval._

  // A specification for evaluating a sum,
  // nothing gets triggered at this point!
  val task = Task { 1 + 1 }


  val future: CancelableFuture[Int] = task.runToFuture

  val res: Int =Await.result(future, 5.seconds)

  println(res)


}
