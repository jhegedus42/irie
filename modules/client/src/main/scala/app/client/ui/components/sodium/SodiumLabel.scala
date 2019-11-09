package app.client.ui.components.sodium

import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import sodium.Cell

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
    .builder[Unit]("SodiumLabel")
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

  class Backend($ : BackendScope[Unit, String]) {

    def render(s: String) = {
      <.div(
        state
      )
    }
  }
}
