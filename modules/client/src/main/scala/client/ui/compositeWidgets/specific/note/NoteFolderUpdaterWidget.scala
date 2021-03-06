package client.ui.compositeWidgets.specific.note

import client.cache.Cache
import client.cache.commands.UpdateEntityInCacheCmd
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.RelationalOperations
import client.cache.relationalOperations.onDataModel.NoteOperations
import client.sodium.core.Cell
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.CellPreformattedText
import client.ui.compositeWidgets.general.{
  CellOptionDisplayerWidget,
  EntitySelectorWidget
}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import shared.dataStorage.model.{Folder, Note}
import shared.dataStorage.relationalWrappers.{
  Ref,
  TypedReferencedValue
}

import scala.collection.immutable
import scala.concurrent.ExecutionContextExecutor

case class NoteFolderUpdaterWidget(
  selectedNote: Cell[Option[TypedReferencedValue[Note]]]) {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  implicit lazy val folderCache: Cache[Folder] =
    Cache.folderCache

  val noteFolderSelectorWidget = EntitySelectorWidget[Folder]({
    x: Folder =>
      <.div(x.name)
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
      noteFolderSelectorWidget.selectedEntityResolved.map(
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
        noteFolderSelectorWidget.selectedEntityResolved
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
    NoteOperations.selectedNotesNoteFolder(selectedNote)
  }

  lazy val selectedNotesNoteFolderDisplayer =
    CellOptionDisplayerWidget[Folder](
      folderContainingSelectedNote
        .map(
          _.versionedEntityValue.valueWithoutVersion
        ).co, { nf: Folder =>
        <.div(
          nf.name,
          <.br
        )
      }
    )

  lazy val allFolders: CellOption[Set[TypedReferencedValue[Folder]]] =
    Cache.getAllEntites[Folder]

  lazy val folderContainingSelectedNote
    : CellOption[TypedReferencedValue[Folder]] = {
    val r1: CellOption[Option[TypedReferencedValue[Folder]]] = for {
      fs <- allFolders
      sn <- CellOption.fromCellOption(selectedNote)

      h: Option[TypedReferencedValue[Folder]] = fs
        .filter(
          _.versionedEntityValue.valueWithoutVersion.notes
            .contains(sn.ref)
        ).headOption

    } yield (h)

    val r2: CellOption[TypedReferencedValue[Folder]] =
      CellOption.flattenOpt(r1)

    r2
  }

  lazy val addNoteToFolderButton: SButton = {

    lazy val currentValOpt: CellOption[List[Ref[Note]]] = {
      CellOption.fromCellOption(
        noteFolderSelectorWidget.selectedEntityResolved.map(
          _.map(_.versionedEntityValue.valueWithoutVersion.notes)
        )
      )
    }

    lazy val selectedFolder: CellOption[Folder] =
      CellOption.fromCellOption(
        noteFolderSelectorWidget.selectedEntityResolved.map(
          _.map(_.versionedEntityValue.valueWithoutVersion)
        )
      )

    def appandNoteRefToEndOfList(
      r:      Ref[Note],
      folder: Folder
    ): Folder = {
      import monocle.macros.syntax.lens._
      val currentList = folder.lens(_.notes).get
      val newList     = currentList :+ r
      folder.lens(_.notes).set(newList)
    }

    lazy val selectedNoteRefCO: CellOption[Ref[Note]] = {
      CellOption
        .fromCellOption(selectedNote).map(
          _.ref
        )
    }

    lazy val newValOpt: CellOption[Folder] = {

      val r: CellOption[Folder] =
        selectedNoteRefCO.lift2(selectedFolder)(
          appandNoteRefToEndOfList
        )
      r
    }

    lazy val updaterButton = SButton(
      "add Note",
      Some(() => {
        lazy val trvOpt: Option[TypedReferencedValue[Folder]] =
          noteFolderSelectorWidget.selectedEntityResolved.sample()
        lazy val newValueOpt: Option[Folder] = newValOpt.co.sample()

        lazy val f1 = folderContainingSelectedNote.co.sample()

        // first you have to remove a note from it's existing folder
        // only after that can you move it to somewhere else...

        if (trvOpt.isDefined && newValueOpt.isDefined & f1.isEmpty) {
          val updateCMD =
            UpdateEntityInCacheCmd[Folder](trvOpt.get,
                                           newValueOpt.get)
          folderCache.updateEntityCommandStream.send(updateCMD)
          println("we should add a Note")
        }

      })
    )
    updaterButton
  }

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Note's Folder updater"),
        <.br,
        <.hr,
        <.br,
        "Select a Folder :",
        <.br,
        noteFolderSelectorWidget.selectorTable.comp(),
        <.hr,
        "The Selected Folder:",
        <.br,
        selectedNoteFolderDisplayer.optDisplayer(),
        <.br,
        <.hr,
        "The selected Note:",
        <.br,
        selectedNoteDisplayer.optDisplayer(),
        <.hr,
        <.br,
        "The Folder that contains the selected Note:",
        selectedNotesNoteFolderDisplayer.optDisplayer(),
        <.hr,
        <.br,
        "Notes contained in the selected Folder:",
        <.br,
        resolvedListOfNotesNamesDisplayerWidget.optDisplayer(),
        <.br,
        addNoteToFolderButton.comp(),
        <.br,
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

// todo-now
//   support for Rect for images

//   1)
//     -- attach image to Note

//  lazy val addSelectedNoteToFolder: EntityUpdaterButton[Folder] = {

//    lazy val newValue: CellOption[Folder] = ???

//    lazy val updaterCommand = ???

//    lazy val removerCommand = ???

//    lazy val compositeCommand = ???

// todo-now

//    EntityUpdaterButton[Folder](
//      CellOption.fromCellOption(
//        noteFolderSelectorWidget.selectedEntity
//      ),
//      folderCache,
//      newValue,
//      "update"
//    )

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

//  }
