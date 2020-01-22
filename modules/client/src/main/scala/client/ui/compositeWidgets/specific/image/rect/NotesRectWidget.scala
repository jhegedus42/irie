package client.ui.compositeWidgets.specific.image.rect

import client.sodium.core.{Cell, CellLoop, Stream, StreamSink}
import client.ui.wrappedReact.{
  Crop,
  ImgCropWidget,
  ReactCropWidgetState
}
import shared.dataStorage.model.{Note, Rect, VisualHint}

case class NotesRectWidget(
  updateImgWithQue: Stream[Option[VisualHint]],
  get:              VisualHint => Rect,
  set:              (VisualHint, Rect) => VisualHint) {

  // sync to and back
  //   from ImageWithQue and ReactCropWidgetState

  lazy val reactCropWidgetStateUpdater
    : Stream[Option[ReactCropWidgetState]] = {
    def f(imageWithQue: VisualHint): ReactCropWidgetState = {
      val r = get(imageWithQue)
      val c = ReactCropWidgetState.rect2Crop(r)
      val i = imageWithQue.fileName
      val s = ReactCropWidgetState(c, i)
      s
    }
    val s: Stream[Option[ReactCropWidgetState]] =
      updateImgWithQue.map(_.map(f))
    s
  }

  lazy val imageWithQueOptionCell: CellLoop[Option[VisualHint]] =
    new CellLoop[Option[VisualHint]]()

  lazy val imageWithQueUpdaterFromCropper
    : Stream[Option[VisualHint]] = {

    def f(
      or:  Option[Rect],
      ovh: Option[VisualHint]
    ): Option[VisualHint] = {
      for {
        vh <- ovh
        r <- or
      } yield (set(vh, r))
    }

    w.internalStateUpdater
      .map(_.map(_.crop)).map(
        _.map(ReactCropWidgetState.crop2Rect(_))
      ).snapshot(imageWithQueOptionCell, f(_,_))
  }

  imageWithQueOptionCell.loop(
    updateImgWithQue.orElse(imageWithQueUpdaterFromCropper).hold(None)
  )

  lazy val w = ImgCropWidget(reactCropWidgetStateUpdater)

  lazy val component = {
    w.comp
  }

}
