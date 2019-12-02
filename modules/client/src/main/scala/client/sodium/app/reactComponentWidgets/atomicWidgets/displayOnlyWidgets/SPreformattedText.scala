package client.sodium.app.reactComponentWidgets.atomicWidgets.displayOnlyWidgets

import client.sodium.core._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  ScalaComponent
}

// TODO-now, replace this with `SWPreformattedTextFromTemplate`
//  after that, delete it completely

case class SPreformattedText(
  s: Stream[String],
  initialState: String =
    "default init state of SPreformattedText") {

  val comp = ScalaComponent
    .builder[Unit]("SodiumPreformattedText")
    .initialState(initialState)
    .renderBackend[Backend]
    .componentWillMount(f => {

      Callback {
        s.listen(x => {
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
