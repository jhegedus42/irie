package client.cache.relationalOperations

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.sodium.core.Cell
import shared.dataStorage.{
  ImageWithQue,
  Note,
  Ref,
  TypedReferencedValue
}

object NoteOperations {

  def getNotesForAnImage(
    coImg: CellOption[TypedReferencedValue[ImageWithQue]]
  ): CellOption[Set[TypedReferencedValue[Note]]] = {

    val criteria
      : CellOption[TypedReferencedValue[Note] => Boolean] = {

      val res: CellOption[TypedReferencedValue[Note] => Boolean] =
        for {
          img <- coImg
          f = (n: TypedReferencedValue[Note]) => {
            img.versionedEntityValue.valueWithoutVersion.referenceToNote
              .map((xn: Ref[Note]) => xn == n.ref)
          }.getOrElse(false)

        } yield f
      res
    }

    val res =
      RelationalOperations.coGetAllEntitiesWithFilter[Note](criteria)

    res
  }

}
