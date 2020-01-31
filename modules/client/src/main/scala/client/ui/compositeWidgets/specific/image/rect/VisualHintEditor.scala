package client.ui.compositeWidgets.specific.image.rect

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.onDataModel.NoteOperations
import client.sodium.core
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import client.ui.compositeWidgets.specific.image.svg.{
  CompositeSVGDisplayer,
  VisualLinkAsSVGHelpers
}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import monocle.macros.syntax.lens._
import monocle.syntax.ApplyLens
import shared.dataStorage.model.{Note, Rect, HintForNote}
import shared.dataStorage.relationalWrappers.TypedReferencedValue
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div

case class VisualHintEditor(
  selectedNote: CellOption[TypedReferencedValue[Note]]) {

  val optNoteStream: core.Stream[Option[Note]] = selectedNote.co
    .updates().map(_.map(_.versionedEntityValue.valueWithoutVersion))

  val rectHintToThisEditor = {
    def get(n: Note) = n.lens(_.visualHint.rectForHead.rect).get

    def set(
      n: Note,
      r: Rect
    ) =
      n.lens(_.visualHint.rectForHead.rect).set(r)

    val comp = HintCropEditorWidget(selectedNote, get, set)
    comp
  }

  val placeOfHintToNextEditor = {
    def get(n: Note) =
      n.lens(_.visualHint.rectForTail.rect).get

    def set(
      n: Note,
      r: Rect
    ) =
      n.lens(_.visualHint.rectForTail.rect).set(r)

    lazy val comp = HintCropEditorWidget(selectedNote, get, set)
    comp
  }

  val nextNoteTitleDisplayer = {
    import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

    lazy val nextNote: CellOption[TypedReferencedValue[Note]] =
      NoteOperations.getNextNote(selectedNote)
    CellOptionDisplayerWidget[Note](
      nextNote.map(_.versionedEntityValue.valueWithoutVersion).co, {
        (next: Note) =>
          {
            <.div(<.h3("Next Note's title:"),
                  <.br,
                  s"${next.title}",
                  <.br)
          }
      }
    ).optDisplayer
  }


  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.br,
        "Rect Editor:",
        <.h4(
          "Cropped hint to this Note's VisualHint to be placed into previous Note's Hint:"
        ),
        <.br,
        rectHintToThisEditor.vdom,
        <.h4("Place of hint to next Note:"),
        <.br,
        placeOfHintToNextEditor.vdom,
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
