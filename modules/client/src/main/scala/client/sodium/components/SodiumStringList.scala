package client.sodium.components

import client.sodium.core._
import client.sodium.components.SodiumStringList.State
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.VdomElement

case class SodiumStringList(input: Stream[State]) {

  def renderer(state: State): VdomElement = {
    <.div(state.string.toString())
  }

  val ct
    : (Stream[State], String, State, State => VdomElement) => ComponentTemplate[State] =
    ComponentTemplate[State]

  val comp = ct(input, "SodiumStringList", State(), renderer(_))

  def getComp = comp.comp()

}

object SodiumStringList {
  case class State(string: List[String] = List())
}
