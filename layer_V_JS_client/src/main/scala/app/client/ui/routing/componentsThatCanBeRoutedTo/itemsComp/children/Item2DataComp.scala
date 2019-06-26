package app.client.ui.routing.componentsThatCanBeRoutedTo.itemsComp.children

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Item2DataComp {

  val component =
    ScalaComponent.builder.static("Item2")(<.div("This is Item2 Page ")).build

  def apply() = component().vdomElement
}
