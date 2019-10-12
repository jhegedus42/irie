package app.client
import app.client.ui.components.router.RouterComp
import app.shared.utils.macros.compilationTime.AppendCompilationTimeToString
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element

import scala.concurrent.ExecutionContextExecutor
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport( "Main" )
object Main extends js.JSApp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  @JSExport
  def main(): Unit = {
    routedApp()
  }

  def routedApp(): Unit = {
    val e: Element = document.getElementById( "rootComp" )

    println(
      s"Main.routedApp() : Router is just about to be mounted into a DIV."
    )

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
