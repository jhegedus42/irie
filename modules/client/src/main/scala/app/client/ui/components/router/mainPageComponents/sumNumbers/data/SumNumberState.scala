package app.client.ui.components.router.mainPageComponents.sumNumbers.data

import app.shared.dataModel.views.SumIntView.SumIntView_Par
import monocle.macros.Lenses

@Lenses
case class SumNumberState(
    tn:             TheThieveryNumber,
    sumIntViewPars: SumIntView_Par
)