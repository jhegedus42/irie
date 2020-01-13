package client.ui.compositeWidgets.note

import client.cache.Cache
import client.sodium.core.Cell
import client.ui.atomicWidgets.show.text.CellPreformattedText
import client.ui.compositeWidgets.general.{
  CellOptionDisplayerWidget,
  EntitySelectorWidget
}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import shared.dataStorage.{Note, NoteFolder, TypedReferencedValue}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

import scala.concurrent.ExecutionContextExecutor

case class NoteFolderUpdaterWidget(
  selectedNote: Cell[Option[TypedReferencedValue[Note]]]) {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  implicit lazy val noteFolderCache: Cache[NoteFolder] =
    Cache.noteFolderCache

  val selector = EntitySelectorWidget[NoteFolder]({ x: NoteFolder =>
    x.name
  })

  val selectedNoteDisplayer =
    CellOptionDisplayerWidget[Note](
      selectedNote.map(
        _.map(_.versionedEntityValue.valueWithoutVersion)
      ), { n: Note =>
        <.div(n.title)
      }
    )

  val selectedNoteFolderDisplayer =
    CellOptionDisplayerWidget[NoteFolder](
      selector.selectedEntity.map(
        _.map(_.versionedEntityValue.valueWithoutVersion)
      ), { nf: NoteFolder =>
        <.div(nf.name)
      }
    )

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Note Folder Updater"),
        <.br,
        selector.selectorTable.comp(),
        selectedNoteFolderDisplayer.displayer(),
        selectedNoteDisplayer.displayer(),
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
