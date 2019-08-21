package app.client.ui.components.router.mainPageComponents.sumNumbers.data

import app.shared.comm.postRequests.SumIntPostRequest.SumIntPar
import monocle.macros.Lenses

@Lenses
case class SumNumberState(
    tn:             TheThieveryNumber,
    sumIntViewPars: SumIntPar
)
