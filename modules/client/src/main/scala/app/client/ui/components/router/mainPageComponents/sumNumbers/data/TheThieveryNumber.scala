package app.client.ui.components.router.mainPageComponents.sumNumbers.data

import monocle.macros.Lenses

@Lenses
private[sumNumbers] case class TheThieveryNumber(
    firstNumber:  Int,
    secondNumber: Int
)
