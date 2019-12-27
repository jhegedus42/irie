package client.ui.compositeWidgets.note

import client.cache.{Cache, CacheMap}
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{Note, TypedReferencedValue, User}

import scala.concurrent.ExecutionContextExecutor

case class NoteCreateReadUpdateWidget() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val noteCache: Cache[Note] = Cache.noteCache

  lazy val noteCacheMap: CellLoop[CacheMap[Note]] = noteCache.cellLoop

  lazy val selectedNote =
    new CellSink[Option[TypedReferencedValue[Note]]](None)

  lazy val noteSelectorTable = {

    def note2VDOMList(
      u: TypedReferencedValue[Note]
    ): List[VdomElement] = {

      val title =
        <.div(u.versionedEntityValue.valueWithoutVersion.title)

      val selectButton = SButton("select", {
        Some(() => selectedNote.send(Some(u)))
      })

      List(title, selectButton.comp())

    }

    val comp = CellTemplate(
      noteCacheMap, { x: CacheMap[Note] =>
        <.div(
          TableHelpers.getTableFromVdomElements(
            x.map.values.toList
              .map(
                note2VDOMList
              )
          )
        )
      }
    )

    comp
  }

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Note Selector"),
        <.hr,
        <.br,
        noteSelectorTable.comp(),
        NoteCreatorWidget().createNewNoteButton.comp(),
        NoteEditorWidget(selectedNote.updates()).comp(),
        <.hr,
        <.br
      )
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp

  }

}
// todonow 2 - add/update image to a note
// todonow 2.1 upload image

// todonow 3 - add / edit selected rectangle inside the added image
// todonow 4 - notelist editor
