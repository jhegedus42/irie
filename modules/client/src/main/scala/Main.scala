import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import sodium.components.SodiumButton

import scala.concurrent.ExecutionContextExecutor
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport("Main")
object Main extends js.JSApp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  val button=SodiumButton("hello")

  @JSExport
  def main(): Unit = {
    val e: Element = document.getElementById("rootComp")
    button.getVDOM().renderIntoDOM(e)
  }

}
