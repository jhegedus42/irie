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
  TypedReferencedValue
}
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
        <.div(s"${n.title} ${n.folder}")
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

  lazy val entityUpdaterButton
    : EntityUpdaterButton[Note, Option[Ref[NoteFolder]]] = {

    lazy val updaterOpt
      : Cell[Option[(Note, Option[Ref[NoteFolder]]) => Note]] = {

      lazy val note:          Cell[Option[Note]]            = ???
      lazy val noteFolderRef: Cell[Option[Ref[NoteFolder]]] = ???

      lazy val f = { (n: Option[Note], nf: Option[Ref[NoteFolder]]) =>
        ??? : Option[(Note, Option[Ref[NoteFolder]]) => Note]
      }

      val res: Cell[Option[(Note, Option[Ref[NoteFolder]]) => Note]] =
        note.lift(noteFolderRef, f)

      res
    }

    def extractor(note: Note): Option[Ref[NoteFolder]] = note.folder

    EntityUpdaterButton(selectedNote,
                        Cache.noteCache,
                        extractor(_),
                        updaterOpt,
                        "update")

  }

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Note Folder Updater"),
        <.br,
        selector.selectorTable.comp(),
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
