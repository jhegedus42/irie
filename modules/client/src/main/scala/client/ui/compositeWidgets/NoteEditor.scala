package client.ui.compositeWidgets

import client.cache.{Cache, CacheMap, UpdateEntityInCacheCmd}
import client.ui.atomicWidgets.input.{SButton, STextArea}
import client.ui.atomicWidgets.templates.CellTemplate
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{Note, TypedReferencedValue, User}

import scala.concurrent.ExecutionContextExecutor

case class NoteEditor() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val noteCache: Cache[Note] = Cache.noteCache

  lazy val listOfNotes = SWPreformattedText(
    noteCache.cellLoop
      .updates().map(
        (c: CacheMap[Note]) => c.getPrettyPrintedString
      )
  )

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("List of Notes"),
        <.hr,
        <.br,
        listOfNotes.comp(),
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

// todonow 1 Note CRUD
// todonow 1.1 display set of notes on client (test data)

// todonow 1.1.1 create cache for set of notes on client - IN PROGRESS
// todonow 1.1.1.1 populate Note Entity Cache on client
// todonow 1.1.1.2 refactor Entity populater, abstract over
//  entities

// todonow 1.1.2 display list of notes for Alice

// todonow 1.2 select a note from the list (use the User Selector
//  Widget as template)
// todonow 1.3 display the content of the selected note
// todonow 1.4 create a widget that updates the text of the note
// todonow 1.5 create a widget that creates a new note
// todonow 1.6 create a widget that flags a note as "Moved to TrashCan"
//   todonow 1.7 add trashcan field to a Note
