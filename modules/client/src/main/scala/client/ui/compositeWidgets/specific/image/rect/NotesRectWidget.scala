package client.ui.compositeWidgets.specific.image.rect

import client.sodium.core.{Cell, Stream, StreamSink}
import client.ui.wrappedReact.{
  Crop,
  ImgCropWidget,
  ReactCropWidgetState
}
import shared.dataStorage.model.{VisualHint, Note, Rect}

case class NotesRectWidget(
                            updateImgWithQue: Stream[Option[VisualHint]],
                            get:              VisualHint => Rect,
                            set:              (VisualHint, Rect) => VisualHint) {

  // sync to and back
  //   from ImageWithQue and ReactCropWidgetState

  lazy val reactCropWidgetStateUpdater
    : Stream[Option[ReactCropWidgetState]] = {
    def f(imageWithQue: VisualHint): ReactCropWidgetState = {
      val r=get(imageWithQue)
      val c= ReactCropWidgetState.rect2Crop(r)
      val i=imageWithQue.fileName
      val s=ReactCropWidgetState(c,i)
      s
    }
    val s: Stream[Option[ReactCropWidgetState]] = updateImgWithQue.map(_.map(f))
    s
  }

  lazy val imageWithQueUpdaterFromCropper: Stream[Option[VisualHint]] = {
    ???
  }

  lazy val imageWithQueCell: Cell[Option[VisualHint]] = {
    updateImgWithQue.orElse(imageWithQueUpdaterFromCropper).hold(None)
  }

  lazy val component = {
    val w = ImgCropWidget(reactCropWidgetStateUpdater)
    w.comp
  }

}
