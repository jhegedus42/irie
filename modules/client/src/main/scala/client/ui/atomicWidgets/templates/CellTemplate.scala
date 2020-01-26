package client.ui.atomicWidgets.templates

import client.sodium.core.Cell
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, _}

case class CellTemplate[V](
  cell:     Cell[V],
  renderer: V => VdomElement) {

  val streamTemplate =
    StreamTemplate(cell.updates(),
                   "CellComp",
                   () => cell.sample(),
                   renderer)

  lazy val comp = streamTemplate.comp
}
