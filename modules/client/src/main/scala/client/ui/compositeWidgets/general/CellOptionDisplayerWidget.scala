package client.ui.compositeWidgets.general

import client.cache.Cache
import client.sodium.core.Cell
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.templates.StreamTemplate
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import shared.testingData.TestDataStore
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class CellOptionDisplayerWidget[V](
  entityOptCell: Cell[Option[V]],
  renderer:      V => VdomElement) {

  lazy val optRenderer: Option[V] => html_<^.VdomElement = {
    x: Option[V] =>
      {
        x match {
          case Some(value) => renderer(value)
          case None        => <.div("Entity is not defined.")
        }
      }

  }

  lazy val streamTemplate =
    StreamTemplate[Option[V]](entityOptCell.updates(),
                              "EntityDisplayerWidget",
                              entityOptCell.sample(),
                              optRenderer)

  lazy val displayer = streamTemplate.comp

}
