package client.cache.relationalOperations.onDataModel

import client.cache.Cache
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.RelationalOperations
import client.sodium.core.Cell
import shared.dataStorage.model.{Folder, Note}
import shared.dataStorage.relationalWrappers.{
  Ref,
  TypedReferencedValue
}

object NoteOperations {

  def getNextNote(
    currentNote: CellOption[TypedReferencedValue[Note]]
  ): CellOption[TypedReferencedValue[Note]] = {

    val folderCO = CellOption.fromCellOption(
      selectedNotesNoteFolder(
        currentNote.co
      )
    )

    val noteRefCO = currentNote.map(_.ref)

    val nextNoteRefCO: CellOption[Ref[Note]] =
      FolderOperations.getRefToNextNote(folderCO, noteRefCO)

    val res: CellOption[TypedReferencedValue[Note]] =
      Cache.resolveRefCO(nextNoteRefCO)

    res
  }

  def selectedNotesNoteFolder(
    selectedNote: Cell[Option[TypedReferencedValue[Note]]]
  ): Cell[Option[Folder]] = {

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
      RelationalOperations.getAllEntitiesWithFilter(filterBy)

    val r2: Cell[Option[Folder]] = res.map(
      _.headOption.map(_.versionedEntityValue.valueWithoutVersion)
    )
    r2
  }

}
