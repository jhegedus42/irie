package client.ui.compositeWidgets.general

import client.cache.relationalOperations.CellOptionMonad.CellOption
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^
import org.scalajs.dom.html.Div
import vdom.html_<^._

case class CellOptionListWidget[V](
  lCO:               CellOption[List[V]],
  elementRendererCO: CellOption[V => VdomElement]) {

  lazy val res: CellOption[VdomElement] =
    for {
      l <- lCO
      f <- elementRendererCO
      lvdom = l.map(f)

      vdom = {
        <.ul(
          lvdom.map(<.li(_)).toVdomArray
        )
      }

      vdom2=if(l.isEmpty) {
        <.div(
          "List is empty."
        )
      } else vdom

    } yield (vdom2)


  lazy val comp = CellOptionVDOMWidget(res).comp

}
