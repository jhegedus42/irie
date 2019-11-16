package client.sodium.app.reactComponents

import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import japgolly.scalajs.react.extra.router.StaticDsl.Rule
import japgolly.scalajs.react.extra.router.{RouterConfigDsl, _}
import japgolly.scalajs.react.vdom.html_<^._
import client.sodium.core._

case class SodiumPreformattedText(s: Stream[String]) {

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
