package app.client
import app.client.ui.components.RouterWrapper
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

    RouterWrapper.component().renderIntoDOM( e )

  }


}
