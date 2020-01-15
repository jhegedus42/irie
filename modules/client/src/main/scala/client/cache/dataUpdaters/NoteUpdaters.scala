package client.cache.dataUpdaters

import client.cache.Cache
import client.sodium.core.{
  Cell,
  CellLoop,
  Stream,
  StreamSink,
  Transaction
}
import shared.dataStorage.{
  Note,
  NoteFolder,
  Ref,
  TypedReferencedValue
}

object NoteUpdaters {

//  def setNoteFolderForNote(
//    note:      Cell[Option[TypedReferencedValue[Note]]],
//    newFolder: Cell[Option[Ref[NoteFolder]]]
//  )(
//    implicit
//    nc: Cache[Note],
//    nf: Cache[Ref[NoteFolder]]
//  ): Stream[Unit] = {
//    ???
//  }
}
