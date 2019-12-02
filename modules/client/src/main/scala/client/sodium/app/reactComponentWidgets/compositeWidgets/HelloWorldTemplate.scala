package client.sodium.app.reactComponentWidgets.compositeWidgets

import client.sodium.app.reactComponentWidgets.atomicWidgets.inputWidgets.STextArea
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor

case class HelloWorldTemplate() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def getComp = {

    val userName = STextArea("init_text")

    val render: Unit => VdomElement = { _ =>
      <.div(s"Hello there Unit",
            <.br,
            STextArea("first blood").comp())
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp
  }

}
