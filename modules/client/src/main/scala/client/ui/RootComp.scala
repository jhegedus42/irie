package client.ui

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C
import client.ui.compositeWidgets.archive.CounterExampleWidget
import client.ui.compositeWidgets.image.{ImageList, ImagesWidget}
import client.ui.compositeWidgets.note.NotesWidget
import client.ui.wrappedReact.{Crop, ReactCrop, ReactCropWrapped, TagsInput}

object RootComp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.main(C.container, ^.role := "container")(
          <.div(C.jumbotron)(
            NotesWidget().getComp(),
            <.br,
            ImagesWidget().getComp(),
            <.br,
            TagsInput(value    = Seq("foo", "bar42"),
                      onChange = TagsInput.handlerCore(_)),
            ReactCropWrapped.comp("6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg" )
          )
        )
      )
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp
  }

}
