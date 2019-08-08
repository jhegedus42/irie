package app.client.ui.caching

import app.client.ui.caching.ReRenderer.ReRenderTriggerer
import app.client.ui.components.router.mainPageComponents.sumNumbers.CacheTestComp.SumNumbersExampleComp_NotWrapped_Backend
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumNumbersExample_Props
import japgolly.scalajs.react.component.Scala.Component

class CacheInjectorHOC( toBeWrapped: Component[SumNumbersExample_Props, Unit, SumNumbersExampleComp_NotWrapped_Backend,
                          CtorType.Props ] ) {

  lazy val wrapperConstructor =
    ScalaComponent
      .builder[SumNumbersExample_Props]("Wrapper")
      .renderBackend[WrapperBackend]
      .componentWillMount( $ => Callback { ReRenderer.
        setTriggerer(ReRenderTriggerer(() =>{ $.setState(Unit).runNow()}))})
      .build

  class WrapperBackend($: BackendScope[SumNumbersExample_Props, Unit]) {

    def render(props: SumNumbersExample_Props) = {
      <.div(toBeWrapped(props))
    }
  }

}
