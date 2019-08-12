package app.client.ui.components.router.mainPageComponents.sumNumbers.injected.localState

import app.shared.dataModel.views.SumIntView.SumIntView_Par
import monocle.macros.Lenses

@Lenses
private[sumNumbers] case class State(
    tn:             TheThieveryNumber,
    sumIntViewPars: SumIntView_Par
)
