package client.ui

import client.ui.compositeWidgets.{CounterExampleWidget, TodoList, UserAdminWidget}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C
import client.ui.compositeWidgets.image.ImageList
import client.ui.compositeWidgets.note.NoteCreateReadUpdateWidget

object RootComp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.main(C.container, ^.role := "container")(
          <.div(C.jumbotron)(
            <.h1("test page2"),
            UserAdminWidget().getComp(),
            <.br,
            CounterExampleWidget().getComp(),
            <.br,
            TodoList().getComp(),
            <.br,
            NoteCreateReadUpdateWidget().getComp(),
            <.br,
            <.hr,
            <.h2("Images"),
            <.br,
            <.input(^.id := "the-file",
                    ^.name := "file",
                    ^.`type` := "file"),
            <.input(^.id := "submit-btn",
                    ^.value := "Upload",
                    ^.`type` := "submit"),
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
