package client.sodium.app.reactComponents

import client.sodium.core._
import client.sodium.app.reactComponents.SodiumStringList.State
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.VdomElement

case class SodiumStringList(input: Stream[State]) {

//  val ct
//    : (Stream[State], String, State, State => VdomElement) => SComponentTemplate[State] =

  val comp = SComponentTemplate[State](input,
                                       "SodiumStringList",
                                       State(),
                                       renderer(_))

  def renderer(state: State): VdomElement = {
    <.div(state.string.toString())
  }

  def getComp = comp.comp()

}

object SodiumStringList {
  case class State(string: List[String] = List())
}
