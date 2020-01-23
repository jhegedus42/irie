package client.ui.compositeWidgets.specific.image.rect

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.sodium.core
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import client.ui.compositeWidgets.specific.image.{ImageDisplayerWidget, ImageUploaderWidget}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import monocle.macros.syntax.lens._
import monocle.syntax.ApplyLens
import shared.dataStorage.model.{Note, Rect}
import shared.dataStorage.relationalWrappers.TypedReferencedValue
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div

case class ImgQueEditor(
  selectedNote: CellOption[TypedReferencedValue[Note]]) {

  val optNoteStream: core.Stream[Option[Note]] = selectedNote.co
    .updates().map(_.map(_.versionedEntityValue.valueWithoutVersion))

  val rectHintToThisEditor = {
    def get(n: Note) = n.lens(_.img.hintToThisImage.rect).get

    def set(
      n: Note,
      r: Rect
    ) =
      n.lens(_.img.hintToThisImage.rect).set(r)

    lazy val comp = NotesRectWidget(selectedNote, get, set)
    comp
  }

  val placeOfHintToNextEditor = {
    def get(n: Note) = n.lens(_.img.placeForHintToNextImage.rect).get

    def set(
      n: Note,
      r: Rect
    ) =
      n.lens(_.img.placeForHintToNextImage.rect).set(r)

    lazy val comp = NotesRectWidget(selectedNote, get, set)
    comp
  }

  lazy val imgFileUploader=ImageUploaderWidget(selectedNote)

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.br,
        "Rect Editor:",
        rectHintToThisEditor.vdom,
        placeOfHintToNextEditor.vdom,
//        imgFileUploader.
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

  // todo-now
  //  upload / change image for current note


}
