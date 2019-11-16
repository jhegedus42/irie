package client.sodium.app.reactComponents

import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import japgolly.scalajs.react.extra.router.StaticDsl.Rule
import japgolly.scalajs.react.extra.router.{RouterConfigDsl, _}
import japgolly.scalajs.react.vdom.html_<^._
import client.sodium.core.Cell

case class SodiumLabel(val c: Cell[String]) {

  def sampledValue = c.sample()

  var state = "initial_state"

  var stateSetter: ReRenderTriggerer = ReRenderTriggerer(None)

  val f = c.listen(x => {
    println(s"sodum label's cell is $x");
    state = x
    stateSetter.trigger()
  })

  val comp = ScalaComponent
    .builder[Int]("SodiumLabel")
    .initialState("label")
    .renderBackend[Backend]
    .componentWillMount(f => {
      val g = () => f.setState("bla").runNow()
      Callback {
        val s = ReRenderTriggerer(Some(g))
        stateSetter = s
      }
    })
    .build

  class Backend($ : BackendScope[Int, String]) {

    def render(
      i: Int,
      s: String
    ): VdomElement = <.label("bla")

  }
}
