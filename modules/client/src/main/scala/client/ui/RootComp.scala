package client.ui

import client.sodium.app.reactComponentWidgets.compositeWidgets.{
  CounterExample,
  NewUserCreator,
  TodoList
}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor

import bootstrap4.TB.C

object RootComp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.main(C.container, ^.role := "container")(
          <.div(C.jumbotron)(<.h1("test page"),
                             NewUserCreator().getComp(),
                             <.br,
                             CounterExample().getComp(),
                             <.br,
                             TodoList().getComp())
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
