package client.ui

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C
import client.ui.compositeWidgets.archive.CounterExampleWidget
import client.ui.compositeWidgets.image.ImageList
import client.ui.compositeWidgets.note.NotesWidget

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
            <.h2("Images"),
            <.br,
            ImageList.listOfImages.comp()
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
