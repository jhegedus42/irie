package app.client.ui.components.router.mainPageComponents.sumNumbers.injector

import app.client.ui.components.router.mainPageComponents.sumNumbers.injected.InjectedComp
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{BackendScope}

class InjectorBackend(
    $ : BackendScope[PropsWithCache, Unit]
) {

  def render( props: PropsWithCache ) = {
    val cache = props.cacheInterface
    <.div(
      InjectedComp.component( cache )
    )
  }
}
