package app.client.ui.routing.routersChildren.itemsComp

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Item1DataComp {

  val component =
    ScalaComponent.builder.static("Item1")(<.div("This is Item1 Page ")).build

  def apply() = component().vdomElement
}
