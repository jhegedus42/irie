package client.ui.compositeWidgets.note

import client.cache.Cache
import client.sodium.core.Cell
import client.ui.atomicWidgets.show.text.CellPreformattedText
import client.ui.compositeWidgets.general.{
  CellOptionDisplayerWidget,
  EntitySelectorWidget,
  EntityUpdaterButton
}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import shared.dataStorage.{
  Note,
  NoteFolder,
  Ref,
  TypedReferencedValue,
  VersionedValue
}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

import scala.concurrent.ExecutionContextExecutor

case class NoteFolderUpdaterWidget(
  selectedNote: Cell[Option[TypedReferencedValue[Note]]]) {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  implicit lazy val noteFolderCache: Cache[NoteFolder] =
    Cache.noteFolderCache

  val noteFolderSelectorWidget = EntitySelectorWidget[NoteFolder]({
    x: NoteFolder =>
      x.name
  })

  val selectedNoteDisplayer =
    CellOptionDisplayerWidget[Note](
      noteFolderSelectorWidget.selectedEntity.map(
        _.map(_.versionedEntityValue.valueWithoutVersion)
      ), { n: Note =>
        <.div(s"${n.title} ${n.folder}")
      }
    )

  val selectedNoteFolderDisplayer =
    CellOptionDisplayerWidget[NoteFolder](
      noteFolderSelectorWidget.selectedEntity.map(
        _.map(_.versionedEntityValue.valueWithoutVersion)
      ), { nf: NoteFolder =>
        <.div(nf.name)
      }
    )

  lazy val entityUpdaterButton
    : EntityUpdaterButton[Note, Option[Ref[NoteFolder]]] = {

    def extractor(note: Note): Option[Ref[NoteFolder]] = note.folder

    val newValue: Cell[Option[Ref[NoteFolder]]] =
      noteFolderSelectorWidget.selectedEntity.map(_.map(_.ref))

    EntityUpdaterButton[Note, Option[Ref[NoteFolder]]](
      selectedNote,
      Cache.noteCache,
      extractor(_),
      newValue,
      "update"
    )

  }

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Note Folder Updater"),
        <.br,
        noteFolderSelectorWidget.selectorTable.comp(),
        selectedNoteFolderDisplayer.displayer(),
        selectedNoteDisplayer.displayer(),
        // todo now - add here updater button
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
