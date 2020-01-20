package client.ui.compositeWidgets.specific.image.rect

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import client.ui.compositeWidgets.specific.image.ImageDisplayerWidget
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import monocle.macros.syntax.lens._
import monocle.syntax.ApplyLens
import shared.dataStorage.model.{Note, Rect}
import shared.dataStorage.relationalWrappers.TypedReferencedValue
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div

case class RectEditor(
  selectedNote: CellOption[TypedReferencedValue[Note]],
  get:          Note => Rect,
  set:          (Note,Rect) => Note) {

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      def renderRect(r: Rect): VdomTagOf[Div] = {
        <.div(s"center:", r.center.toString)
      }

      val rectWidget = CellOptionDisplayerWidget(
        selectedNote
          .map(_.versionedEntityValue.valueWithoutVersion).map(get).co,
        renderRect(_)
      )

      <.div(
        <.br,
        "Rect Editor:",
        rectWidget.displayer(),
        <.br
      )

    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("RectEditWidget")
        .render_P(render)
        .build

    rootComp

  }

}
