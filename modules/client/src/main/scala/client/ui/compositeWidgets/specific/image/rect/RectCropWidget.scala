package client.ui.compositeWidgets.specific.image.rect

import client.sodium.core.{Cell, Stream, StreamSink}
import shared.dataStorage.model.{ImageWithQue, Note, Rect}

case class RectCropWidget(
  note:   Cell[Note],
  get: Note => Rect,
  set: (Note, Rect => Note)) {



  // todo-now
  //  continue here ...
  //
  //  1) display Note's Rect
  //
  //  2) turn React state into Cell
  //
  //  3) update Note's Rect
  //     with Update button
  //

}
