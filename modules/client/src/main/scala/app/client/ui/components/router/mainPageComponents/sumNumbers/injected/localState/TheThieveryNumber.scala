package app.client.ui.components.router.mainPageComponents.sumNumbers.injected.localState

import monocle.macros.Lenses

@Lenses
private[sumNumbers] case class TheThieveryNumber(
    firstNumber:  Int,
    secondNumber: Int
)
