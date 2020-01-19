package client.ui.compositeWidgets.general

import client.cache.relationalOperations.CellOptionMonad.CellOption
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  ScalaComponent,
  vdom
}
import client.sodium.core.{Cell, Stream}
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

case class CellOptionVDOMWidget(c: CellOption[VdomElement]) {

  lazy val comp = ScalaComponent
    .builder[Unit]("VDOMWidget")
    .initialState(c.co.sample())
    .renderBackend[Backend]
    .componentWillMount(f => {

      Callback {
        c.co.listen((x: Option[VdomElement]) => {
          println(s"sodum label's cell is $x");
          f.setState(x).runNow()
        })
      }

    })
    .build

  class Backend($ : BackendScope[Unit, Option[VdomElement]]) {

    import vdom.html_<^._

    def render(
      unit:  Unit,
      state: Option[VdomElement]
    ): VdomElement = {
      if (state.isDefined) {
        state.get
      } else <.p("Note defined.")
    }

  }

}
