package client.ui.helpers.vdom

import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

object ComposingVDOM {

  def concatenateVDOMElements(
    l: List[VdomElement]
  ): TagMod = {
    TagMod(l.toVdomArray)
  }

}
