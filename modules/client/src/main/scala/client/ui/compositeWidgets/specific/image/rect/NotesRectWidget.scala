package client.ui.compositeWidgets.specific.image.rect

import client.sodium.core.{Cell, Stream, StreamSink}
import client.ui.wrappedReact.{
  Crop,
  ImgCropWidget,
  ReactCropWidgetState
}
import shared.dataStorage.model.{ImageWithQue, Note, Rect}

case class NotesRectWidget(
  updateImgWithQue: Stream[ImageWithQue],
  get:              ImageWithQue => Rect,
  set:              (ImageWithQue, Rect) => ImageWithQue) {

  // sync to and back
  //   from ImageWithQue and ReactCropWidgetState

  lazy val reactCropWidgetStateUpdater
    : Stream[ReactCropWidgetState] = {
    def f(imageWithQue: ImageWithQue): ReactCropWidgetState = {
      val r=get(imageWithQue)
      val c= ReactCropWidgetState.rect2Crop(r)
      val i=imageWithQue.fileName
      ???
    }
    val s: Stream[ReactCropWidgetState] = updateImgWithQue.map(f)
    s
  }

  lazy val imageWithQueUpdater: Stream[ImageWithQue] = ???

  lazy val imageWithQueCell: Cell[ImageWithQue] = ???

  lazy val component = {
    val w = ImgCropWidget(reactCropWidgetStateUpdater)
    w.comp
  }

}
