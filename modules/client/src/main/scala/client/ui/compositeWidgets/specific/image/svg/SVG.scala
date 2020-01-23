package client.ui.compositeWidgets.specific.image.svg

import shared.dataStorage.model.VisualHint

case class SVG(background:VisualHint,hint:VisualHint){

  def asString:String = ???

  lazy val svgExample={
    """
      |<svg width="100" height="150">
      |
      |  <circle cx="50" cy="50" r="40"
      |     stroke="green" stroke-width="4" fill="yellow"
      |  />
      |
      |  <circle cx="50" cy="70" r="40"
      |     stroke="green" stroke-width="4" fill="red"
      |  />
      |
      |Sorry, your browser does not support inline SVG.
      |
      |</svg>
      |""".stripMargin
  }
}

object SVG {
  lazy val backgroundExample="6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg"
  lazy val hintExample="befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg"

}
