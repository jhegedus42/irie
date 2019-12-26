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
import shared.testingData.TestDataStore

case class NoteCreatorWidget() {

  lazy val createNewNoteButton = SButton(
    "Create New Note",
    Some(() => {
      println("i was pusssshed")

      val newNote =
        TypedReferencedValue[Note](
          Note("default title",
               "default content",
               TestDataStore.aliceEnt.ref)
        )

      Cache.noteCache.insertEntityStream.send(newNote)
    })
  )

}
