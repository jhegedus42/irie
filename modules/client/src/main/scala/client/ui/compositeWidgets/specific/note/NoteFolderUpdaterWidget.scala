package client.ui.compositeWidgets.specific.note

import client.cache.Cache
import client.cache.commands.UpdateEntityInCacheCmd
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

    // if note is not in any folder

    lazy val currentValOpt: CellOption[List[Ref[Note]]] = {
      CellOption.fromCellOption(
        noteFolderSelectorWidget.selectedEntity.map(
          _.map(_.versionedEntityValue.valueWithoutVersion.notes)
        )
      )
    }

    lazy val selectedFolder: CellOption[Folder] =
      CellOption.fromCellOption(
        noteFolderSelectorWidget.selectedEntity.map(
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

    lazy val newValOpt: CellOption[List[Ref[Note]]] = {

//      selectedFolder.map(_.)

      // todo write lift2 for Cell Option

      ???

    }

//    ???
    lazy val updaterButton = SButton(
      "add Note",
      Some({ () =>
        {
//        lazy val trvOpt      = selectedEntityOpt.co.sample()
//        lazy val newValueOpt = newValOpt.co.sample()
//        if (trvOpt.isDefined && newValueOpt.isDefined) {
//          val updateCMD =
//            UpdateEntityInCacheCmd[V](trvOpt.get, newValueOpt.get)
//          cache.updateEntityCommandStream.send(updateCMD)
          println("we should add a Note")
        }
      })
    )
    ???
  }

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
        selectedNoteFolderDisplayer.displayer(),
        <.br,
        <.hr,
        "The selected Note:",
        <.br,
        selectedNoteDisplayer.displayer(),
        <.hr,
        <.br,
        "The Folder that contains the selected Note:",
        selectedNotesNoteFolderDisplayer.displayer(),
        <.hr,
        <.br,
        "Notes contained in the selected Folder:",
        <.br,
        resolvedListOfNotesNamesDisplayerWidget.displayer(),
//        addSelectedNoteToFolder.comp(),
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
