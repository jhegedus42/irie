package app.client
import app.client.ui.components.sodium.wrappers.LoginSwitcher
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element

import scala.concurrent.ExecutionContextExecutor
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport("Main")
object Main extends js.JSApp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  val loginSwitcher= LoginSwitcher()

  @JSExport
  def main(): Unit = {
    val e: Element = document.getElementById("rootComp")
    loginSwitcher.comp(10).renderIntoDOM(e)
  }


}
