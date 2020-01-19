package client.cache.relationalOperations

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.sodium.core.Cell
import shared.dataStorage.{ImageWithQue, Note, TypedReferencedValue}

object NoteOperations {

  def getNotesForAnImage(
    coImg: CellOption[TypedReferencedValue[ImageWithQue]]
  ): CellOption[Set[TypedReferencedValue[Note]]] = {



    val criteria : CellOption[Note=>Boolean] = {

      for {
        img <-coImg
//        f = (n:Note) => n.
      } yield 2

      ???
    }

    val res =
      RelationalOperations.coGetAllEntitiesWithFilter[Note](???)

   res
  }

}
