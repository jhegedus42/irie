package client.ui.atomicWidgets.show.text

import client.sodium.core._
import client.ui.atomicWidgets.show.text

case class SWPreformattedText(
  s: Stream[String],
  initialState: String =
    "default init state of SPreformattedText") {

  val is =
    s.map(SWPreformattedTextFromTemplate.State.apply(_))

  val ic =
    text.SWPreformattedTextFromTemplate(is, initialState)

  val comp = ic.comp.comp
}
