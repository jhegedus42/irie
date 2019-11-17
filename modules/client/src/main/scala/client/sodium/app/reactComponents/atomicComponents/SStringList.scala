package client.sodium.app.reactComponents.atomicComponents

import client.sodium.app.reactComponents.atomicComponents.SStringList.State
import client.sodium.core._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, _}

case class SStringList(input: Stream[State]) {

  val comp = StreamTemplate[State](input,
                                   "SodiumStringList",
                                   State(),
                                   renderer(_))

  def renderer(state: State): VdomElement = {
    <.div(state.string.toString())
  }

}

object SStringList {
  case class State(string: List[String] = List())
}
