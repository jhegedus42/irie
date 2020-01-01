package client.ui.compositeWidgets.note

import client.cache.{Cache, CacheMap, UpdateEntityInCacheCmd}
import client.sodium.core.{Cell, CellLoop, CellSink}
import client.ui.atomicWidgets.input.{SButton, STextArea}
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{Note, TypedReferencedValue, User}
import monocle.macros.syntax.lens._

import scala.concurrent.ExecutionContextExecutor
import client.sodium.core.{CellLoop, Stream, StreamSink, Transaction}

case class NoteEditorWidget(
  selectedNoteStream: Stream[Option[TypedReferencedValue[Note]]]) {

  val internalUpdaterOfSelectedNote =
    new StreamSink[Option[TypedReferencedValue[Note]]]()

  val m = selectedNoteStream.orElse(internalUpdaterOfSelectedNote)

  val internalSelectedNoteCell = m.hold(None)

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val noteCache: Cache[Note] = Cache.noteCache

  val selectedNodesTitle = m.map(
    _.map(_.versionedEntityValue.valueWithoutVersion.title)
      .getOrElse("")
  )

  val noteTitleEditorField = STextArea("", selectedNodesTitle)

  lazy val updateButton = SButton(
    "update title",
    Some({ () =>
      {
        val cmdOpt: Option[UpdateEntityInCacheCmd[Note]] = {
          val newTitle            = noteTitleEditorField.cell.sample()
          val currentSelectedNote = internalSelectedNoteCell.sample()
          val res = currentSelectedNote.map(x => {
            UpdateEntityInCacheCmd[Note](
              x,
              x.versionedEntityValue.valueWithoutVersion
                .lens(
                  _.title
                ).set(newTitle)
            )
          })
          res
        }

        if (cmdOpt.isDefined) {
          val cmd = cmdOpt.get
          noteCache.updateEntityCommandStream.send(cmd)
          internalUpdaterOfSelectedNote.send(None)
        }

      }
    })
  )

  def comp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.br,
        "title:",
        noteTitleEditorField.comp(),
        updateButton.comp(),
        <.br
      )
    }

    val comp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    comp

  }

}
