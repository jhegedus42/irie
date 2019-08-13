package app.client.ui.components.router.mainPageComponents.sumNumbers.injected.localState

import app.shared.dataModel.views.SumIntView.SumIntView_Par
import monocle.macros.Lenses
import monocle.syntax._

@Lenses
case class SumNumberState(
    tn:             TheThieveryNumber,
    sumIntViewPars: SumIntView_Par
)

