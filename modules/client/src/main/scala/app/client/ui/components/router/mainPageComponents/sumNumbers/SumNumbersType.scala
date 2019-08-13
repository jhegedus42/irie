package app.client.ui.components.router.mainPageComponents.sumNumbers

import app.client.ui.caching.cacheInjector.ToBeWrappedComponent
import app.client.ui.components.router.mainPageComponents.sumNumbers.data.{SumNumberState, SumNumbersProps}

trait SumNumbersType extends ToBeWrappedComponent[SumNumbersType] {
  override type Props=SumNumbersProps
  override type Backend = SumNumbersBackend[Props]
  override type State = SumNumberState

}
