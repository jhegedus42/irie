package sandbox.monix

  object MonixDemo {
    import scala.concurrent.duration._

    def monixExample(): Unit = {

      import monix.execution.CancelableFuture

      // make this into an App if you want to run it
      // App is commented out because then sbt has a default, single
      // App to launch, so there is no need to select what to launch
      // manually each time the server is restarted

      // todo later - https://monix.io/docs/3x/intro/hello-world.html

      // We need a scheduler whenever asynchronous
      // execution happens, substituting your ExecutionContext

      import monix.execution.Scheduler.Implicits.global
      //    implicit def executionContext: ExecutionContextExecutor =
      //      scala.scalajs.concurrent.JSExecutionContext.Implicits._

      //    import scala.scalajs.concurrent.JSExecutionContext._
      //    import scala.scalajs.concurrent.JSExecutionContext.Implicits._
      //    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue._
      //


      // Needed below
      import monix.eval._

      // A specification for evaluating a sum,
      // nothing gets triggered at this point!
      val task = Task { 1 + 1 }

      val future: CancelableFuture[Int] = task.runToFuture

      future.onComplete(x => println(s"result for monix demo $x"))

    }

    def monixDemo2():Unit = {
      import monix.execution.Scheduler.Implicits.global
      import monix.reactive._
      // Nothing happens here, as observable is lazily
      // evaluated only when the subscription happens!
      val tick = {
        Observable.interval(1.second)
          // common filtering and mapping
          .filter(_ % 2 == 0)
          .map(_ * 2)
          // any respectable Scala type has flatMap, w00t!
          .flatMap(x => Observable.fromIterable(Seq(x,x)))
          // only take the first 5 elements, then stop
          .take(5)
          // to print the generated events to console
          .dump("Out")
      }
      val cancelable = tick.subscribe()

    }

}
