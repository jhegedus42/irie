package app.client.ui.components.router.mainPageComponents.sumNumbers

import app.client.ui.caching.CacheInterface
import japgolly.scalajs.react.CtorType
import japgolly.scalajs.react.component.Scala.Component

case class SumNumbersExample_Props( s: String, cacheInterface: CacheInterface )

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object CacheTestComp {

  lazy val compConstructor: Component[
    SumNumbersExample_Props,
    Unit,
    SumNumbersExampleComp_NotWrapped_Backend,
    CtorType.Props
  ] =
    ScalaComponent
      .builder[SumNumbersExample_Props]( "Cache Experiment" )
      .renderBackend[SumNumbersExampleComp_NotWrapped_Backend]
      .build

  class SumNumbersExampleComp_NotWrapped_Backend(
      $ : BackendScope[SumNumbersExample_Props, Unit]
  ) {

    def render( props: SumNumbersExample_Props ) = {
      val cache = props.cacheInterface
      <.div(
        SumNumberCompInjected.TheCorporation( cache )
      )
    }
  }

}
