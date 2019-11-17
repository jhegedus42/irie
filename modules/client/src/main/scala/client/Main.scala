package client

import client.cache.TestAjaxRequest
import client.ui.{RootComp, Router}
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

    RootComp.getComp("Joco2").renderIntoDOM(e)

    TestAjaxRequest.query()
  }
}
