package client.ui.compositeWidgets.specific.note

import client.cache.Cache
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.RelationalOperations
import client.sodium.core.Cell
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.CellPreformattedText
import client.ui.compositeWidgets.general.{
  CellOptionDisplayerWidget,
  EntitySelectorWidget,
  EntityUpdaterButton
}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import shared.dataStorage.{
  Folder,
  Note,
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

  implicit lazy val folderCache: Cache[Folder] =
    Cache.folderCache

  val noteFolderSelectorWidget = EntitySelectorWidget[Folder]({
    x: Folder =>
      x.name
  })

  val selectedNoteDisplayer =
    CellOptionDisplayerWidget[Note](
      selectedNote.map(
        _.map(_.versionedEntityValue.valueWithoutVersion)
      ), { n: Note =>
        <.div(
          s"Selected Note's title: ${n.title}"
        )
      }
    )

  lazy val selectedNoteFolderDisplayer =
    CellOptionDisplayerWidget[Folder](
      noteFolderSelectorWidget.selectedEntity.map(
        _.map(_.versionedEntityValue.valueWithoutVersion)
      ), { nf: Folder =>
        <.div(
          <.br,
          s"Selected NoteFolder's name:",
          nf.name,
          <.br,
          s"Notes in The Folder",
          <.pre(
            nf.notes
              .foldLeft("")({ (s, rn) =>
                s + s"${rn.unTypedRef.toString}\n"
              }).toString()
          ),
          <.br
        )
      }
    )

  lazy val noteNames: CellOption[List[String]] = {
    val ref: CellOption[TypedReferencedValue[Folder]] =
      CellOption.fromCellOption(
        noteFolderSelectorWidget.selectedEntity
      )

    val notes: CellOption[List[Ref[Note]]] =
      ref.map(_.versionedEntityValue.valueWithoutVersion.notes)

    val res1: CellOption[List[TypedReferencedValue[Note]]] =
      RelationalOperations.resolveListOfRefOptions[Note](notes)

    val res2: CellOption[List[String]] = res1.map(
      _.map(_.versionedEntityValue.valueWithoutVersion.title)
    )
    res2
  }

  lazy val resolvedListOfNotesNamesDisplayerWidget =
    CellOptionDisplayerWidget[
      List[String]
    ](
      noteNames.co, { nf: List[String] =>
        <.div(
          s"Notes in The Selected Note Folder",
          <.pre(
            nf.foldLeft("")({ (s, rn) =>
                s + s"$rn\n"
              }).toString()
          ),
          <.br
        )
      }
    )

  lazy val selectedNotesNoteFolder: Cell[Option[Folder]] = {

    val filterBy: Cell[Folder => Boolean] = {
      selectedNote.map((xtrv: Option[TypedReferencedValue[Note]]) => {
        val xOpt: Option[Ref[Note]] =
          xtrv.map(_.ref)
        (f: Folder) => {

          def g: Ref[Note] => Boolean = { rn: Ref[Note] =>
            f.notes.contains(rn)
          }
          val res = xOpt.map(g)
          res.getOrElse(false)
        }
      })
    }
    val res: Cell[Set[TypedReferencedValue[Folder]]] =
      RelationalOperations.filterTable(filterBy)

    val r2: Cell[Option[Folder]] = res.map(
      _.headOption.map(_.versionedEntityValue.valueWithoutVersion)
    )
    r2
  }

  lazy val selectedNotesNoteFolderDisplayer =
    CellOptionDisplayerWidget[Folder](
      selectedNotesNoteFolder, { nf: Folder =>
        <.div(
          <.br,
          s"Selected Note's NoteFolder's name:",
          nf.name,
          <.br
        )
      }
    )

  lazy val addSelectedNoteToFolder: EntityUpdaterButton[Folder] = {

    lazy val newValue: CellOption[Folder] = ???

    lazy val allFolders
      : CellOption[Set[TypedReferencedValue[Folder]]] =
      Cache.getAllEntites[Folder]

    lazy val folderContainingSelectedNote
      : CellOption[TypedReferencedValue[Folder]] = ???

    lazy val updaterCommand = ???

    lazy val removerCommand = ???

    lazy val compositeCommand = ???

    // todo-now

    EntityUpdaterButton[Folder](
      CellOption.fromCellOption(
        noteFolderSelectorWidget.selectedEntity
      ),
      folderCache,
      newValue,
      "update"
    )

    // todo :
    //
    //   We need to send a composite command when pushing
    //   the button:
    //
    //    1) remove the selected note from its current Folder
    //       (if there is any)
    //
    //    2) add the selected note to the selected Folder
    //

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
        resolvedListOfNotesNamesDisplayerWidget.displayer(),
        addSelectedNoteToFolder.comp(),
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
