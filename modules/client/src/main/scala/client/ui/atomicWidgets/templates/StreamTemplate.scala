package client.ui.atomicWidgets.templates

import client.sodium.core.Stream
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

case class StreamTemplate[V](
  input:         Stream[V],
  componentName: String,
  initialState:  () => V,
  renderer:      V => VdomElement) {

  lazy val comp = ScalaComponent
    .builder[Unit](componentName)
    .initialState(initialState())
    .renderBackend[Backend]
    .componentWillMount(f => {

      Callback {

        input.listen((x: V) => {
          println(s"sodum label's cell is $x");
          f.setState(x).runNow()
        })

      } >> f.setState(initialState())

    })
    .build

  class Backend($ : BackendScope[Unit, V]) {

    def render(
      unit:  Unit,
      state: V
    ): VdomElement = renderer(state)
  }

}
