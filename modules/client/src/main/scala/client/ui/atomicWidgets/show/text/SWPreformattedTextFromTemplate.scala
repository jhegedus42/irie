package client.ui.atomicWidgets.show.text

import client.sodium.core._
import client.ui.atomicWidgets.show.text.SWPreformattedTextFromTemplate.State
import client.ui.atomicWidgets.templates.StreamTemplate
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

case class SWPreformattedTextFromTemplate(
  input: Stream[State],
  initialState: String =
    "default init state of SWPreformattedTextFromTemplate") {

  val comp =
    StreamTemplate[State](input,
                          "SWPreformattedTextFromTemplate",
                          () => State(initialState),
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
