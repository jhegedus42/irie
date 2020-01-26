package client.ui.compositeWidgets.specific.image

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.onDataModel.NoteOperations
import client.cache.{Cache, CacheMap}
import client.sodium.core.{Cell, CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.compositeWidgets.general.{CellOptionDisplayerWidget, EntitySelectorWidget, EntityUpdaterButton, TextFieldUpdaterWidget}
import client.ui.compositeWidgets.specific.image.rect.VisualHintEditor
import client.ui.helpers.table.TableHelpers
import io.circe.Encoder
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

import scala.concurrent.ExecutionContextExecutor
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import monocle.syntax.ApplyLens
import shared.dataStorage.model.{Note, Rect, HintForNote}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class ImagesForANote(
  val selectedNote: CellOption[TypedReferencedValue[Note]]) {

  val selectedVisualHint: CellOption[HintForNote] =
    selectedNote.map(_.versionedEntityValue.valueWithoutVersion.visualHint)


  lazy val imgQueEditor = {
    import monocle.macros.syntax.lens._
    VisualHintEditor(
      selectedNote
    )
  }

  lazy val visualHintDisplayer = {
    import monocle.macros.syntax.lens._

    def renderer(
      visualHint: HintForNote
    )(
      implicit
      vh: Encoder[HintForNote]
    ): VdomElement = {

      lazy val s = vh.apply(visualHint).spaces4
      <.pre(s)

    }

    lazy val res: CellOptionDisplayerWidget[HintForNote] =
      CellOptionDisplayerWidget[HintForNote](
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
