package client

import client.cache.comm.TestAjaxRequest
import client.ui.RootComp
import client.ui.router.Router
import org.scalajs.{dom => d}
import org.scalajs.dom.raw.Element

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport("Main")
object Main extends js.JSApp {

  @JSExport
  def main(): Unit = {

    Router.disableBackButton()

    val e: Element = d.document
      .getElementById("rootComp")

    RootComp.getComp().renderIntoDOM(e)

    TestAjaxRequest.query()
  }
}
