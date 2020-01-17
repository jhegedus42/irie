package client.ui.compositeWidgets.specific.note

import client.cache.Cache
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
  Note,
  Folder,
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

  implicit lazy val noteFolderCache: Cache[Folder] =
    Cache.noteFolderCache

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

//  lazy val selectedNotesNoteFolder: Cell[Option[NoteFolder]] = {
//
//    val sn: Cell[Option[Ref[NoteFolder]]] =
//      selectedNote.map((x: Option[TypedReferencedValue[Note]]) => {
//        x.flatMap(
//          (z: TypedReferencedValue[Note]) => {
//            val res1: Option[Ref[NoteFolder]] =
//              z.versionedEntityValue.valueWithoutVersion.locationInNoteFolderOpt.map(_.nf)
//            res1
//          }
//        )
//      })
//
//    val res: Cell[Option[TypedReferencedValue[NoteFolder]]] =
//      Cache.resolveRef(sn)
//    res.map(_.map(_.versionedEntityValue.valueWithoutVersion))
//  }

//  lazy val selectedNotesNoteFolderDisplayer =
//    CellOptionDisplayerWidget[NoteFolder](
//      selectedNotesNoteFolder, { nf: NoteFolder =>
//        <.div(
//          <.br,
//          s"Selected Note's NoteFolder's name:",
//          nf.name,
//          <.br
//        )
//      }
//    )

//  lazy val entityUpdaterButton
//    : SButton = {
//
//    def setter(
//      nf: Option[Ref[NoteFolder]],
//      n:  Note
//    ): Note = {
////      n.copy(folderR = nf)
//      ??? // todo-now
//    }
//
//    val newValue: Cell[Option[Ref[NoteFolder]]] =
//      noteFolderSelectorWidget.selectedEntity.map(_.map(_.ref))
//
//    EntityUpdaterButton[Note, Option[Ref[NoteFolder]]](
//      selectedNote,
//      Cache.noteCache,
//      setter,
//      newValue,
//      "update"
//    )
//
//    ???
//  }

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Note Folder Updater"),
        <.br,
        noteFolderSelectorWidget.selectorTable.comp(),
        selectedNoteFolderDisplayer.displayer(),
        selectedNoteDisplayer.displayer(),
//        selectedNotesNoteFolderDisplayer.displayer(),
//        entityUpdaterButton.comp(),
//        <.hr,
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
