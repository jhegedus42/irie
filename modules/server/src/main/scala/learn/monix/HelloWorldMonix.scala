package learn.monix

import monix.execution.CancelableFuture

object HelloWorldMonix { // extends  App{
  // make this into an App if you want to run it
  // App is commented out because then sbt has a default, single
  // App to launch, so there is no need to select what to launch
  // manually each time the server is restarted

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
