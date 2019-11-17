package client.ui

import client.cache.{Cache, CacheMap, CacheProvider}
import client.sodium.app.actions.SActionWriteToConsole
import client.sodium.app.reactComponents.atomicComponents.{
  SButton,
  SPreformattedText,
  STextArea
}
import client.sodium.app.reactComponents.compositeComponents.{
  CounterExample,
  HelloWorldTemplate,
  NewUserCreator
}
import dataStorage.User
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor

object RootComp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(NewUserCreator().getComp(), <.br, CounterExample().getComp())
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp
  }

  // todo-now, add new user
  // SodiumAction - Insert New Entity

}
