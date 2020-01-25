package client.ui.compositeWidgets.specific.image.rect

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.onDataModel.NoteOperations
import client.sodium.core
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import client.ui.compositeWidgets.specific.image.svg.SVGDemo
import client.ui.compositeWidgets.specific.image.{ImageDisplayerWidget, ImageUploaderWidget}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import monocle.macros.syntax.lens._
import monocle.syntax.ApplyLens
import shared.dataStorage.model.{Note, Rect, VisualHint}
import shared.dataStorage.relationalWrappers.TypedReferencedValue
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div

case class VisualHintEditor(
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

    lazy val comp = HintCropEditorWidget(selectedNote, get, set)
    comp
  }

  val placeOfHintToNextEditor = {
    def get(n: Note) = n.lens(_.img.placeForHintToNextImage.rect).get

    def set(
      n: Note,
      r: Rect
    ) =
      n.lens(_.img.placeForHintToNextImage.rect).set(r)

    lazy val comp = HintCropEditorWidget(selectedNote, get, set)
    comp
  }

  lazy val hintDisplayer = {
    //    import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
    import japgolly.scalajs.react.vdom.html_<^.{<, _}
    lazy val coHint: CellOption[VisualHint] = selectedNote.map(
      _.versionedEntityValue.valueWithoutVersion.img
    )
    CellOptionDisplayerWidget(coHint.co, { (h: VisualHint) =>
      <.div(
        <.br,
        "cropped hint to be placed into the following image:",
        <.br,
        SVGDemo.imgInSVGWithViewBox(h)
      )
    })
  }

  lazy val nextNoteTitleDisplayer = {
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
        nextNoteTitleDisplayer(),
        hintDisplayer.optDisplayer(),
        "Rect Editor:",
        rectHintToThisEditor.vdom,
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
