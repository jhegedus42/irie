package app.client
import app.client.ui.components.router.RouterComp
import app.shared.utils.macros.compilationTime.AppendCompilationTimeToString
import org.scalajs.dom.{Window, document}
import org.scalajs.dom.raw.Element

import scala.concurrent.ExecutionContextExecutor
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport("Main")
object Main extends js.JSApp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  @JSExport
  def main(): Unit = {
    routedApp()
//    outWatchDemo()
  }

  def outWatchDemo(): Unit = {
//    import outwatch.dom._
//    import outwatch.dom.dsl._
//    import monix.execution.Scheduler.Implicits.global
//    val myComponent = div("Hello World")
//
//    OutWatch.renderReplace("#rootComp", myComponent).unsafeRunSync()
  }

  def routedApp(): Unit = {
    val e: Element = document.getElementById("rootComp")

    println(
      s"Main.routedApp() : Router is just about to be mounted into a DIV."
    )
    MonixDemo.monixExample()

    val router = RouterComp().routerComp()
    // todo-later ^^^ have a login page first, when the page loads / reloads
    // if the user is logged in, then it mounts the router
    // if the user is not logged in then it mounts the log in page
    // => log in page will need to umount itself and mount the router instead

    // or the router itself is a child of a react component

//    RouterWrapper.component().renderIntoDOM( e )

    router.renderIntoDOM(e)

  }
}

object MonixDemo {

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
    import scala.concurrent.Await
    import scala.concurrent.duration._

    import monix.eval._

    // A specification for evaluating a sum,
    // nothing gets triggered at this point!
    val task = Task { 1 + 1 }

    val future: CancelableFuture[Int] = task.runToFuture

    future.onComplete(x => println(s"result for monix demo $x"))

  }

}
