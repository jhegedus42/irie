package client.cache.relationalOperations.onDataModel

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.sodium.core.Cell
import shared.dataStorage.model.{Folder, Note}
import shared.dataStorage.relationalWrappers.Ref

object FolderOperations {

  def getNextNote(
    folderCO:  CellOption[Folder],
    noteRefCO: CellOption[Ref[Note]]
  ): CellOption[Ref[Note]] = {
    def g(
      nr: Ref[Note],
      f:  Folder
    ): Option[Ref[Note]] = {
      val l   = f.notes
      val ls  = l.dropWhile(_.unTypedRef.uuid != nr.unTypedRef.uuid)
      val res = ls.headOption
      res
    }

    val res: CellOption[Option[Ref[Note]]] =
      noteRefCO.lift2(folderCO)(g(_, _))

    val res2: Cell[Option[Ref[Note]]] = res.co.map(_.flatten)

    CellOption.fromCellOption(res2)
  }
}
