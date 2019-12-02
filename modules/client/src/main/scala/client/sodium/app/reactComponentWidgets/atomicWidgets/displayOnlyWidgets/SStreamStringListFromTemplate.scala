package client.sodium.app.reactComponentWidgets.atomicWidgets.displayOnlyWidgets

import client.sodium.app.reactComponentWidgets.atomicWidgets.displayOnlyWidgets.SStreamStringListFromTemplate.State
import client.sodium.app.reactComponentWidgets.atomicWidgets.templates.StreamTemplate
import client.sodium.core._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, _}

case class SStreamStringListFromTemplate(
  input: Stream[State]) {

  val comp =
    StreamTemplate[State](input,
                          "SodiumStringList",
                          State(),
                          renderer(_))

  def renderer(state: State): VdomElement = {
    <.div(state.string.toString())
  }
}

object SStreamStringListFromTemplate {
  case class State(string: List[String] = List())
}
