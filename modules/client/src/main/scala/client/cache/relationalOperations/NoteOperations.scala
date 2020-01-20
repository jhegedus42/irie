package client.cache.relationalOperations

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.RelationalOperations.ResultSet
import client.sodium.core.Cell
import shared.dataStorage.{
  ImageWithQue,
  Note,
  Ref,
  TypedReferencedValue,
  Value
}

object NoteOperations {

  def getNotesForAnImage(
    coImg: CellOption[TypedReferencedValue[ImageWithQue]]
  ): CellOption[ResultSet[Note]] = {

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

  def getImagesForANote(
    coNote: CellOption[TypedReferencedValue[Note]]
  ): CellOption[ResultSet[ImageWithQue]] = {

    val criteria
      : CellOption[TypedReferencedValue[ImageWithQue] => Boolean] = {

      val res
        : CellOption[TypedReferencedValue[ImageWithQue] => Boolean] =
        for {
          note <- coNote

          f = (i: TypedReferencedValue[ImageWithQue]) => {
            i.versionedEntityValue.valueWithoutVersion.referenceToNote
              .map({ rn: Ref[Note] =>
                note.ref == rn
              })
          }.getOrElse(false)

        } yield f

      res
    }

    val res: CellOption[Set[TypedReferencedValue[ImageWithQue]]] =
      RelationalOperations.coGetAllEntitiesWithFilter[ImageWithQue](
        criteria
      )

    res
  }

  def getNoteImageUpdaterCompositeCommand(
    note: CellOption[TypedReferencedValue[Note]],
    img:  CellOption[TypedReferencedValue[ImageWithQue]]
  ) = {
    // todo-now - write this function
    ???
  }

}
