package client.sodium.app.reactComponentWidgets.atomicWidgets.displayOnlyWidgets

import client.sodium.core._

case class SPreformattedText(
  s: Stream[String],
  initialState: String =
    "default init state of SPreformattedText") {

  val is =
    s.map(SWPreformattedTextFromTemplate.State.apply(_))

  val ic = SWPreformattedTextFromTemplate(is, initialState)

  val comp = ic.comp.comp
}
