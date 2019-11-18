package client.sodium.app.reactComponents.atomicComponents
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

import client.sodium.core.{Cell, Stream}
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

case class CellPreformattedText(cell: Cell[String]) {

  val comp = ScalaComponent
    .builder[Unit]("SodiumPreformattedText")
    .initialState(cell.sample())
    .renderBackend[Backend]
    .componentWillMount(f => {

      Callback {
        cell.listen(x => {
          println(s"sodium label's cell is $x");
          f.setState(x).runNow()
        })
      }

    })
    .build

  class Backend($ : BackendScope[Unit, String]) {

    def render(
      i:    Unit,
      text: String
    ): VdomElement = <.pre(text)

  }

}
