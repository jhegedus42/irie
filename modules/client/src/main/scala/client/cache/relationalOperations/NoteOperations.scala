package client.cache.relationalOperations

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.RelationalOperations.ResultSet
import client.sodium.core.Cell
import shared.dataStorage.model.{ImageWithQue, Note}
import shared.dataStorage.relationalWrappers.{Ref, TypedReferencedValue}

object NoteOperations {





  def getNoteImageUpdaterCompositeCommand(
    note: CellOption[TypedReferencedValue[Note]],
    img:  CellOption[TypedReferencedValue[ImageWithQue]]
  ) = {
    // todo-now - write this function
    ???
  }



}
