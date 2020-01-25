package client.ui.compositeWidgets.specific.image

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.onDataModel.NoteOperations
import client.cache.{Cache, CacheMap}
import client.sodium.core.{Cell, CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.compositeWidgets.general.{CellOptionDisplayerWidget, EntitySelectorWidget, EntityUpdaterButton, TextFieldUpdaterWidget}
import client.ui.compositeWidgets.specific.image.rect.ImgQueEditor
import client.ui.helpers.table.TableHelpers
import io.circe.Encoder
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

import scala.concurrent.ExecutionContextExecutor
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import monocle.syntax.ApplyLens
import shared.dataStorage.model.{Note, Rect, VisualHint}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class ImagesForANote(
  val selectedNote: CellOption[TypedReferencedValue[Note]]) {

  val selectedVisualHint: CellOption[VisualHint] =
    selectedNote.map(_.versionedEntityValue.valueWithoutVersion.img)

  lazy val editImageTitle = {
    def updaterFunction(
      n: Note,
      s: String
    ): Note = {
      import monocle.macros.syntax.lens._
      n.lens(_.img.title).set(s)
    }

    lazy val extractorFunction: Note => String = (n: Note) => {

      import monocle.macros.syntax.lens._
      val l: ApplyLens[Note, Note, String, String] =
        n.lens(_.img.title)
      l.get
    }
    lazy val noteCO: Cell[Option[TypedReferencedValue[Note]]] =
      selectedNote.co

    TextFieldUpdaterWidget(
      "Image's title:",
      noteCO,
      Cache.noteCache,
      extractorFunction,
      updaterFunction(_, _)
    )

  }

  lazy val imgQueEditor = {
    import monocle.macros.syntax.lens._
    ImgQueEditor(
      selectedNote
    )
  }

  lazy val visualHintDisplayer = {
    import monocle.macros.syntax.lens._

    def renderer(
      visualHint: VisualHint
    )(
      implicit
      vh: Encoder[VisualHint]
    ): VdomElement = {

      lazy val s = vh.apply(visualHint).spaces4
      <.pre(s)

    }

    lazy val res: CellOptionDisplayerWidget[VisualHint] =
      CellOptionDisplayerWidget[VisualHint](
        selectedVisualHint.co,
        renderer(_)
      )

    res
  }

  lazy val imageUploaderWidget = ImageUploaderWidget(selectedNote)

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Images"),
        <.br,
        <.br,
        editImageTitle.comp(),
        imgQueEditor.getComp(),
        imageUploaderWidget.comp.optDisplayer(),
        visualHintDisplayer.optDisplayer(),
        <.hr
      )
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("ImagesWidget")
        .render_P(render)
        .build

    rootComp

  }

}
