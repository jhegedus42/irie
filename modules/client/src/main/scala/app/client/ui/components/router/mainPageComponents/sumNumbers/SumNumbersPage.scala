package app.client.ui.components.router.mainPageComponents.sumNumbers

import app.client.ui.caching.cacheInjector.ToBeWrappedComponent
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumNumbersPage.{SumNumberState, SumNumbersProps}

trait SumNumbersPage extends ToBeWrappedComponent[SumNumbersPage] {
  override type Props   = SumNumbersProps
  override type Backend = SumNumbersBackend[Props]
  override type State   = SumNumberState

}

object SumNumbersPage {
  import app.shared.comm.postRequests.SumIntRoute.SumIntPar
  import monocle.macros.Lenses

  @Lenses
  case class SumNumberState(
    tn:             TheThieveryNumber,
    sumIntViewPars: SumIntPar)

  @Lenses
  private[sumNumbers] case class TheThieveryNumber(
    firstNumber:  Int,
    secondNumber: Int)

  case class SumNumbersURLPayload(string: String)
  case class SumNumbersProps(string:      String)
}
