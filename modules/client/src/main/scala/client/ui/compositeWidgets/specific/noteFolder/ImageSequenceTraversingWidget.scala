package client.ui.compositeWidgets.specific.noteFolder

import client.cache.Cache
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.sodium.core.{Cell, StreamSink, Stream}
import client.ui.compositeWidgets.general.EntitySelectorWidget
import client.ui.compositeWidgets.specific.image.svg.CompositeSVGDisplayer
import client.ui.compositeWidgets.specific.noteFolder.ImageSequenceTraversingWidget.State
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import japgolly.scalajs.react.vdom.VdomElement
import shared.dataStorage.model.{Folder, Note}
import shared.dataStorage.relationalWrappers.{
  Ref,
  TypedReferencedValue
}
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import japgolly.scalajs.react.component.Scala.{Component, Unmounted}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

case class ImageSequenceTraversingWidget() {

  lazy val noteFolderSelectorWidget = EntitySelectorWidget[Folder]({
    x: Folder =>
      x.name
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

  lazy val selectedNoteRefCell: Cell[Option[Ref[Note]]] =
    selectedNoteRefSetter.hold(None)

  // we need to set this to a cell-loop => ...

  lazy val visualLinkDisplayer = {

    val resolvedNote: Cell[Option[TypedReferencedValue[Note]]] =
      Cache.resolveRef(selectedNoteRefCell)

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
      <.h3("ImageSequenceTraversingWidget"),
      <.br,
      noteFolderSelectorWidget.selectorTable.comp(),
      <.br,
      visualLinkDisplayer.visualLinkAsVDOM

      // todo-now - add button here to go to next note...

    )
  }

}

object ImageSequenceTraversingWidget {

  case class State(
    folder: Option[Folder],
    note:   Option[Ref[Note]])
}
