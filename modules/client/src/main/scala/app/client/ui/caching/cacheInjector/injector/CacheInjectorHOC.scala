package app.client.ui.caching.cacheInjector.injector

import app.client.ui.caching.cacheInjector.ReRenderer.ReRenderTriggerer
import app.client.ui.caching.cacheInjector.{
  CacheInterfaceWrapper,
  ReRenderer
}
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^._

private[cacheInjector] class CacheInjectorHOC[Backend, Props, State](
  toBeWrapped: Component[CacheInterfaceWrapper[Props], State, Backend, CtorType.Props]) {

  lazy val wrapperConstructor =
    ScalaComponent
      .builder[CacheInterfaceWrapper[Props]]("Wrapper")
      .renderBackend[WrapperBackend]
      .componentWillMount(
        $ =>
          Callback {
            ReRenderer.setTriggerer(ReRenderTriggerer(() => {
              $.setState(Unit).runNow()
            }))
          }
      )
      .build

  class WrapperBackend(
    $ : BackendScope[CacheInterfaceWrapper[Props], Unit]) {

    def render(props: CacheInterfaceWrapper[Props]) = {
      <.div(toBeWrapped(props))
    }
  }

}
