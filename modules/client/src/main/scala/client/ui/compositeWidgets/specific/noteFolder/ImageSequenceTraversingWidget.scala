package client.ui.compositeWidgets.specific.noteFolder

import client.cache.relationalOperations.CellOptionMonad.CellOption
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

case class ImageSequenceTraversingWidget(
  selectedFolder: CellOption[TypedReferencedValue[Folder]]) {

  lazy val comp = ScalaComponent
    .builder[Unit]("ImageSequenceTraversingWidget")
    .initialState(State(None, None))
    .renderBackend[Backend]
    .componentWillMount(f => {

      Callback {

        selectedFolder.co
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
        "ImageSequenceTraversingWidget",
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
