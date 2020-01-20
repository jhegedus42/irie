package client.cache.dataUpdaters

import client.cache.{Cache, CacheMap}
import client.sodium.core.{Cell, CellLoop, Stream, StreamSink, Transaction}
import client.ui.atomicWidgets.input.SButton
import shared.dataStorage.model.{Folder, Note}
import shared.dataStorage.relationalWrappers.{Ref, TypedReferencedValue}

import scala.collection.immutable

object NoteUpdaters {

  def moveNoteIntoNoteFolderButton(
    noteC:   Cell[Option[TypedReferencedValue[Note]]],
    folderC: Cell[Option[Ref[Folder]]]
  ): SButton = {
    ???

  }

}
