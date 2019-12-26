package client.ui.compositeWidgets.note

import client.cache.{Cache, CacheMap}
import client.sodium.core.{Cell, CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{Note, TypedReferencedValue, User}

import scala.concurrent.ExecutionContextExecutor

case class NoteEditorWidget(
  selectedNote: Cell[Option[TypedReferencedValue[Note]]]) {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val noteCache: Cache[Note] = Cache.noteCache

  lazy val selectedNoteAsText =
    new CellTemplate[Option[TypedReferencedValue[Note]]](
      selectedNote, { x =>
        <.pre(s"selected Note: $x")
      }
    )

  def comp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Note Editor:"),
        <.hr,
        <.br,
        selectedNoteAsText.comp(),
        <.hr,
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

// todonow 1 Note CRUD
// todonow 1.1 display set of notes on client (test data) - DONE

// todonow 1.1.1 create cache for set of notes on client -  DONE
// todonow 1.1.1.1 populate Note Entity Cache on client - DONE
// todonow 1.1.1.2 refactor Entity populater, abstract over DONE
//  entities - DONE

// todonow 1.1.2 display list of notes for Alice - DONE

// todonow 1.2 select a note from the list (use the User Selector
//  Widget as template)
// todonow 1.3 display the content of the selected note
// todonow 1.4 create a widget that updates the text of the note
// todonow 1.5 create a widget that creates a new note
// todonow 1.6 create a widget that flags a note as "Moved to TrashCan"
//   todonow 1.7 add trashcan field to a Note
