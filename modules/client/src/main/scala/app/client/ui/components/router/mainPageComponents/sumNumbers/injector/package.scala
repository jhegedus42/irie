package app.client.ui.components.router.mainPageComponents.sumNumbers
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}

package object injector {
  lazy val injector: Component[
    PropsWithCache,
    Unit,
    InjectorBackend,
    CtorType.Props
    ] =
    ScalaComponent
      .builder[PropsWithCache]( "injector component" )
      .renderBackend[InjectorBackend]
      .build

}
