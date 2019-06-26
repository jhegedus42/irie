package app.client.ui.routing.componentsThatCanBeRoutedTo.itemsComp.children

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object ItemsInfoComp {

  val component = ScalaComponent.builder
    .static("ItemsInfo")(<.div(" Items Root Page  "))
    .build

  def apply() = component().vdomElement
}
