package client.ui

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C
import client.ui.compositeWidgets.archive.CounterExampleWidget
import client.ui.compositeWidgets.image.{ImageList, ImagesWidget}
import client.ui.compositeWidgets.note.NotesWidget
import client.ui.wrappedReact.{Crop, ReactCrop, TagsInput}

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
            ReactCrop(
              src = "6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg",
              crop = new Crop {
                override val unit:   String = "px"
                override val x:      Int    = 10
                override val y:      Int    = 10
                override val width:  Int    = 50
                override val height: Int    = 50
              },
              onChange = ReactCrop.handlerCore(_)
            )
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
