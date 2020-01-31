package client.ui.compositeWidgets.specific.noteFolder

import client.cache.Cache
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.onDataModel.{FolderOperations, NoteOperations}
import client.sodium.core.{Cell, CellLoop, Stream, StreamSink, Transaction}
import client.ui.atomicWidgets.input.SButtonSendClick
import client.ui.compositeWidgets.general.EntitySelectorWidget
import client.ui.compositeWidgets.specific.image.svg.CompositeSVGDisplayer
import client.ui.compositeWidgets.specific.noteFolder.ImageSequenceTraversingWidget.State
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import japgolly.scalajs.react.vdom.VdomElement
import shared.dataStorage.model.{Folder, Note}
import shared.dataStorage.relationalWrappers.{Ref, TypedReferencedValue}
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import japgolly.scalajs.react.component.Scala.{Component, Unmounted}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

case class ImageSequenceTraversingWidget() {

  lazy val noteFolderSelectorWidget = EntitySelectorWidget[Folder]({
    x: Folder =>
      <.div(x.name)
  })

  lazy val selectedNoteRefSetter: Stream[Option[Ref[Note]]] = {

    val res1: Cell[Option[Option[Ref[Note]]]] =
      noteFolderSelectorWidget.selectedEntityResolved.map(
        _.map(
          _.versionedEntityValue.valueWithoutVersion.notes.headOption
        )
      )

    val res2: Cell[Option[Ref[Note]]] = res1.map(_.flatten)

    res2.updates()
  }

  lazy val clickGoToNextNote = new StreamSink[Unit]()

  lazy val selectedNoteRefCellLoop =
    Transaction.apply[CellLoop[Option[Ref[Note]]]] { _ =>
      lazy val selectedNoteRefCellLoop: CellLoop[Option[Ref[Note]]] =
        new CellLoop[Option[Ref[Note]]]()

      lazy val nextNoteRefCellOption: CellOption[Ref[Note]] = {

        val folder =
          CellOption.fromCellOption(
            noteFolderSelectorWidget.selectedEntityResolved.map(
              _.map(_.versionedEntityValue.valueWithoutVersion)
            )
          )

        FolderOperations.getRefToNextNote(
          folder,
          CellOption.fromCellOption(selectedNoteRefCellLoop)
        )
      }

      lazy val getNextNoteStream: Stream[Option[Ref[Note]]] =
        clickGoToNextNote.snapshot(nextNoteRefCellOption.co)

      selectedNoteRefCellLoop.loop(
        selectedNoteRefSetter.orElse(getNextNoteStream).hold(None)
      )

      selectedNoteRefCellLoop
    }

  lazy val visualLinkDisplayer = {

    val resolvedNote: Cell[Option[TypedReferencedValue[Note]]] =
      Cache.resolveRef(selectedNoteRefCellLoop)

    lazy val selectedNoteTypedRefValCellOption =
      CellOption[TypedReferencedValue[Note]](resolvedNote)

    CompositeSVGDisplayer(
      selectedNoteTypedRefValCellOption
    )
  }

  lazy val stateCell: CellOption[State] =
    new CellOption(new Cell[Option[State]](None))

  lazy val nextNote = visualLinkDisplayer.nextNote

  lazy val vdom = {
    <.div(
      <.h3("Image Seq."),
      <.br,
      noteFolderSelectorWidget.selectorTable.comp(),
      <.br,
      visualLinkDisplayer.visualLinkAsVDOM,
      <.br,
        SButtonSendClick("Go to Next Note", clickGoToNextNote).comp(),
      <.br
    )
  }

}

object ImageSequenceTraversingWidget {

  case class State(
    folder: Option[Folder],
    note:   Option[Ref[Note]])
}
