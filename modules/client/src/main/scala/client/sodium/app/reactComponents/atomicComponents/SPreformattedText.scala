package client.sodium.app.reactComponents.atomicComponents

import client.sodium.core._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

case class SPreformattedText(s: Stream[String]) {

  val comp = ScalaComponent
    .builder[Unit]("SodiumPreformattedText")
    .initialState("label")
    .renderBackend[Backend]
    .componentWillMount(f => {

      Callback {
        s.listen(x => {
          println(s"sodum label's cell is $x");
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
