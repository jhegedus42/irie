package app.client.ui.caching

import app.client.ui.caching.ReRenderer.ReRenderTriggerer
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import app.client.ui.components.router.mainPageComp.cacheTestMPC.{
  CacheTest_RootComp_Props,
  NotWrapped_CacheTestRootComp_Backend
}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.component.builder.Lifecycle

class CacheInjectorHOC(
                        toBeWrapped: Component[
                          CacheTest_RootComp_Props,
                          Unit,
                          NotWrapped_CacheTestRootComp_Backend,
                          CtorType.Props ]
                      ) {

  lazy val wrapperConstructor =
    ScalaComponent
      .builder[CacheTest_RootComp_Props]("Wrapper")
      .renderBackend[WrapperBackend]
      .componentWillMount( $ => Callback { ReRenderer.
        setTriggerer(ReRenderTriggerer(() =>{ $.setState(Unit).runNow()}))})
      .build

  class WrapperBackend($: BackendScope[CacheTest_RootComp_Props, Unit]) {

    def render(props: CacheTest_RootComp_Props) = {
      <.div(toBeWrapped(props))
    }
  }

}
