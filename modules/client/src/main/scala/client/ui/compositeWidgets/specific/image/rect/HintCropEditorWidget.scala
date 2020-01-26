package client.ui.compositeWidgets.specific.image.rect

import client.cache.Cache
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.onDataModel.NoteOperations
import client.sodium.core.{Cell, CellLoop, Stream, StreamSink, Transaction}
import client.ui.compositeWidgets.general.{CellOptionDisplayerWidget, EntityUpdaterButton}
import client.ui.compositeWidgets.specific.image.svg.VisualLinkAsSVGHelpers
import client.ui.wrappedReact.{Crop, ImgCropWidget, ReactCropWidgetState}
import japgolly.scalajs.react.vdom.TagOf
import org.scalajs.dom.html.Div
import shared.dataStorage.model.{Note, Rect, VisualHint}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class HintCropEditorWidget(
  selectedNoteCell: CellOption[TypedReferencedValue[Note]],
  get: Note         => Rect,
  set: (Note, Rect) => Note) {

  // sync to and back
  //   from ImageWithQue and ReactCropWidgetState

  lazy val reactCropWidgetStateUpdater
    : Stream[Option[ReactCropWidgetState]] = {
    def f(note: Note): ReactCropWidgetState = {
      val r = get(note)
      val c = ReactCropWidgetState.rect2Crop(r)
      val i = note.visualHint.imgHintToThisNotesText.fileName
      val s = ReactCropWidgetState(c, i)
      s
    }
    val s: Stream[Option[ReactCropWidgetState]] =
      selectedNoteCell
        .map(_.versionedEntityValue.valueWithoutVersion).map(f).co.updates()
    s
  }

  lazy val updateNewNoteCellOnChangeOfSelectedNote
    : Stream[Option[Note]] =
    selectedNoteCell
      .map(_.versionedEntityValue.valueWithoutVersion).co.updates()

  lazy val updaterButton = {

    val updater: CellOption[Note => Note] = CellOption
      .fromCellOption(newNoteOptCell).map({ x =>
        {
          def f(n: Note): Note = x
          f _
        }
      })

    lazy val comp = EntityUpdaterButton(selectedNoteCell,
                                        Cache.noteCache,
                                        updater,
                                        "Update Rect")
    comp
  }

  lazy val newNoteOptCell =
    Transaction.apply[CellLoop[Option[Note]]](_ => {

      lazy val cellLoop: CellLoop[Option[Note]] =
        new CellLoop[Option[Note]]()

      lazy val noteUpdaterFromCropper: Stream[Option[Note]] = {

        def f(
          or:  Option[Rect],
          ovh: Option[Note]
        ): Option[Note] = {
          for {
            vh <- ovh
            r <- or
          } yield (set(vh, r))
        }

        imgCropWidget.internalStateUpdater
          .map(_.map(_.crop)).map(
            _.map(ReactCropWidgetState.crop2Rect(_))
          ).snapshot(cellLoop, f(_, _))
      }

      cellLoop.loop(
        updateNewNoteCellOnChangeOfSelectedNote
          .orElse(noteUpdaterFromCropper).hold(None)
      )

      cellLoop
    })

  lazy val imgCropWidget = ImgCropWidget(reactCropWidgetStateUpdater)

  import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

  lazy val vdom: TagOf[Div] = {
    import japgolly.scalajs.react.vdom.html_<^.{<, _}
    <.div(
      <.br,
      imgCropWidget.comp(),
      <.br,
      updaterButton.comp(),
      <.br
    )
  }

}
