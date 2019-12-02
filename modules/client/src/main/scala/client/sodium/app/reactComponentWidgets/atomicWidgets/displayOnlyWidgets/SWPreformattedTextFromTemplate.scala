package client.sodium.app.reactComponentWidgets.atomicWidgets.displayOnlyWidgets

import client.sodium.app.reactComponentWidgets.atomicWidgets.displayOnlyWidgets.SWPreformattedTextFromTemplate.State
import client.sodium.app.reactComponentWidgets.atomicWidgets.templates.StreamTemplate
import client.sodium.core._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  ScalaComponent
}

case class SWPreformattedTextFromTemplate(
  input: Stream[State],
  initialState: String =
    "default init state of SWPreformattedTextFromTemplate") {

  val comp =
    StreamTemplate[State](input,
                          "SWPreformattedTextFromTemplate",
                          State(initialState),
                          renderer(_))

  def renderer(state: State): VdomElement = {
    <.pre(state.text)
  }
}

object SWPreformattedTextFromTemplate {

  case class State(
    text: String =
      "default init state of SWPreformattedTextFromTemplated")

}
