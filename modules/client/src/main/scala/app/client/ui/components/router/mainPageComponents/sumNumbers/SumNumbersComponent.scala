package app.client.ui.components.router.mainPageComponents.sumNumbers

import app.client.ui.caching.cacheInjector.CacheInterfaceWrapper
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumNumbersPage.{
  SumNumberState,
  SumNumbersProps,
  TheThieveryNumber
}
import app.shared.comm.postRequests.SumIntRoute.SumIntPar
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.{CtorType, ScalaComponent}

object SumNumbersComponent {

//  private type State = OurState

  private[sumNumbers] var initialState = {
    val tn = TheThieveryNumber(38, 45)
    val siwp: SumIntPar = SumIntPar(38, 45)
    SumNumberState(tn, siwp)
  }

  val component: Component[
    CacheInterfaceWrapper[SumNumbersProps],
    SumNumberState,
    SumNumbersBackend[SumNumbersProps],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheInterfaceWrapper[SumNumbersProps]](
        "TheCorporation"
      )
      .initialState(initialState)
      .renderBackend[SumNumbersBackend[SumNumbersProps]] // ← Use Backend class and backend.render
      .build
  }

}
