package app.client.ui.caching

import app.client.ui.caching.ReRenderer.ReRenderTriggerer
import app.client.ui.components.router.mainPageComponents.sumNumbers.injector.{
  InjectorBackend,
  PropsWithCache
}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.component.Scala.Component

class CacheInjectorHOC(
    toBeWrapped: Component[PropsWithCache, Unit, InjectorBackend, CtorType.Props]
) {

  lazy val wrapperConstructor =
    ScalaComponent
      .builder[PropsWithCache]( "Wrapper" )
      .renderBackend[WrapperBackend]
      .componentWillMount(
        $ =>
          Callback {
            ReRenderer.setTriggerer( ReRenderTriggerer( () => {
              $.setState( Unit ).runNow()
            } ) )
        }
      )
      .build

  class WrapperBackend( $ : BackendScope[PropsWithCache, Unit] ) {

    def render( props: PropsWithCache ) = {
      <.div( toBeWrapped( props ) )
    }
  }

}
