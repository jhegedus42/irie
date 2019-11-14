package client.sodium.components

import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import japgolly.scalajs.react.extra.router.StaticDsl.Rule
import japgolly.scalajs.react.extra.router.{RouterConfigDsl, _}
import japgolly.scalajs.react.vdom.html_<^._
import client.sodium.core.Cell
import client.sodium.core._
import org.scalajs.dom.html.Div

case class SodiumRootComp(val vdom: VdomTagOf[Div]) {

  var stateSetter: ReRenderTriggerer = ReRenderTriggerer(None)

  val f = updateRootPage.listen(x => {
    stateSetter.trigger()
  })

  lazy val updateRootPage = new StreamSink[Unit]()

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

    def render(i: Int, s: String): VdomElement = vdom

  }
}
