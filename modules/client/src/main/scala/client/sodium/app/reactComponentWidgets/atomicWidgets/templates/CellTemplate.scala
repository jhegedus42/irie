package client.sodium.app.reactComponentWidgets.atomicWidgets.templates

import client.sodium.core.Cell
import japgolly.scalajs.react.vdom.VdomElement

case class CellTemplate[V](
  cell:     Cell[V],
  renderer: V => VdomElement) {

  val streamTemplate =
    StreamTemplate(cell.updates(), "CellComp", cell.sample(), renderer)

  def getComp = streamTemplate.comp
}
