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
      selectedNote.map(
        _.map(_.versionedEntityValue.valueWithoutVersion)
      ), { n: Note =>
        <.div(s"Selected Note's title: ${n.title}",
              <.br,
              (s"Selected Note's NoteFolder's Ref: ${n.folderR}"))
      }
    )

  lazy val selectedNoteFolderDisplayer =
    CellOptionDisplayerWidget[NoteFolder](
      noteFolderSelectorWidget.selectedEntity.map(
        _.map(_.versionedEntityValue.valueWithoutVersion)
      ), { nf: NoteFolder =>
        <.div(
          <.br,
          s"Selected NoteFolder's name:",
          nf.name,
          <.br
        )
      }
    )

  lazy val selectedNotesNoteFolder: Cell[Option[NoteFolder]] = {

    val sn: Cell[Option[Ref[NoteFolder]]] =
      selectedNote.map((x: Option[TypedReferencedValue[Note]]) => {
        x.flatMap(
          (z: TypedReferencedValue[Note]) => {
            val res1: Option[Ref[NoteFolder]] =
              z.versionedEntityValue.valueWithoutVersion.folderR
            res1
          }
        )
      })

    val res: Cell[Option[TypedReferencedValue[NoteFolder]]] =Cache.resolveRef(sn)
    res.map(_.map(_.versionedEntityValue.valueWithoutVersion))
  }

  lazy val selectedNotesNoteFolderDisplayer =
    CellOptionDisplayerWidget[NoteFolder](
      selectedNotesNoteFolder, { nf: NoteFolder =>
        <.div(
          <.br,
          s"Selected Note's NoteFolder's name:",
          nf.name,
          <.br
        )
      }
    )

  lazy val entityUpdaterButton
    : EntityUpdaterButton[Note, Option[Ref[NoteFolder]]] = {

    def setter(
      nf: Option[Ref[NoteFolder]],
      n:  Note
    ): Note = {
      n.copy(folderR = nf)
    }

    val newValue: Cell[Option[Ref[NoteFolder]]] =
      noteFolderSelectorWidget.selectedEntity.map(_.map(_.ref))

    EntityUpdaterButton[Note, Option[Ref[NoteFolder]]](
      selectedNote,
      Cache.noteCache,
      setter,
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
        selectedNotesNoteFolderDisplayer.displayer(),
        entityUpdaterButton.updaterButton.comp(),
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
