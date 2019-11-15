package client

import org.scalajs.dom.document
import org.scalajs.dom.raw.Element

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport("Main")
object Main extends js.JSApp {

  @JSExport
  def main(): Unit = {
    val e: Element = document
      .getElementById("rootComp")

    RootComp.getComp("Joco2").renderIntoDOM(e)

    TestAjaxRequest.query()
  }

}
