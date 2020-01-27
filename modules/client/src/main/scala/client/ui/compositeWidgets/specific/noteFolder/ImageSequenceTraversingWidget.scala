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
//    lazy val noteFolderSelectorWidget
//    .selectedEntityResolved

    // todo-now - implement this in terms of noteFolderSelectorWidget
    // get the first Ref[Note] from that Cell

    ???
  }

  lazy val selectedNoteRefCell: Cell[Option[Ref[Note]]] =
    selectedNoteRefSetter.hold(None)

  lazy val visualLinkDisplayer = {
    lazy val selectedNoteTypedRefValSetter =
      new StreamSink[Option[TypedReferencedValue[Note]]]()

    val resolvedNote: Cell[Option[TypedReferencedValue[Note]]] =
      Cache.resolveRef(selectedNoteRefCell)

    lazy val selectedNoteTypedRefValCellOption =
      CellOption[TypedReferencedValue[Note]](resolvedNote)

    CompositeSVGDisplayer(
      selectedNoteTypedRefValCellOption
    ).visualLinkAsVDOM
  }

  //  Cache.resolveRef()

  lazy val comp = ScalaComponent
    .builder[Unit]("ImageSequenceTraversingWidget")
    .initialState(State(None, None))
    .renderBackend[Backend]
    .componentWillMount(f => {

      Callback {

        noteFolderSelectorWidget.selectedEntityResolved
          .listen((x: Option[TypedReferencedValue[Folder]]) => {
            println(s"sodum label's cell is $x");

            // get first note in folder
            val optRefToNote = for {
              trv <- x
              refToNote <- trv.versionedEntityValue.valueWithoutVersion.notes.headOption
            } yield (refToNote)
            val s =
              State(x.map(_.versionedEntityValue.valueWithoutVersion),
                    optRefToNote)

            f.setState(s).runNow()

          })

      }
    })
    .build

  class Backend($ : BackendScope[Unit, State]) {

    def render(
      unit:  Unit,
      state: State
    ): VdomElement = {
      <.div(
        <.h3("ImageSequenceTraversingWidget"),
        <.br,
        noteFolderSelectorWidget.selectorTable.comp(),
        <.br,
        s"State is: $state"
      )
    }
  }

}

object ImageSequenceTraversingWidget {

  case class State(
    folder: Option[Folder],
    note:   Option[Ref[Note]])
}
