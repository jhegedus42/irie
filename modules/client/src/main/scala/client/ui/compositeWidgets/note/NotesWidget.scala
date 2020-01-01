package client.ui.compositeWidgets.note

import client.cache.{Cache, CacheMap}
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.compositeWidgets.general.EntitySelectorWidget
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{Note, TypedReferencedValue, User}

import scala.concurrent.ExecutionContextExecutor

case class NotesWidget() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val noteCache: Cache[Note] = Cache.noteCache

  val selector= EntitySelectorWidget[Note](noteCache,{x:Note=>x.title})

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Notes"),
        <.br,
        selector.selectorTable.comp(),
        NoteCreatorWidget().createNewNoteButton.comp(),
        NoteEditorWidget(selector.selectedEntity.updates()).comp(),
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

// use this :
// https://github.com/knoldus/akka-http-file-upload/blob/master/src/main/scala/com/rishi/FileUpload.scala

// todonow 2.1 upload image

// todonow 3 - add / edit selected rectangle inside the added image
// todonow 4 - notelist editor
