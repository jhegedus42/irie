package client.ui.router

import org.scalajs.{dom => d}

import scala.scalajs.js

object Router {
  // https://stackoverflow.com/questions/25806608/how-to-detect-browser-back-button-event-cross-browser
  // https://www.scala-js.org/doc/interoperability/types.html

  def disableBackButton(): Unit = {
    js.Dynamic.literal("page" -> 1)
    val l = js.Dynamic.literal
    val l1: js.Object with js.Dynamic = l("page" -> 1)
    d.window.history.pushState(l1, """, """)

    d.window.onpopstate = {
      a =>

        println(a)
        d.window.history.go(1)
    }
  }
}
