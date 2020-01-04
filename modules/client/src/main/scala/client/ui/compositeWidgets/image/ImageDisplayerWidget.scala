package client.ui.compositeWidgets.image

import client.cache.Cache
import client.sodium.core.Cell
import client.ui.compositeWidgets.general.OptionalEntityDisplayerWidget
import shared.dataStorage.{Image, Value}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

case class ImageDisplayerWidget[V <: Value[V]](
  img: Cell[Image]
)(
  implicit
  cache: Cache[V]) {

  lazy val comp = EntityDisplayerWidget[Image](img, { x: Image =>
    <.div(
      <.hr,
      <.br,
      s"Image's title: ${x.title}",
      <.br,
      <.hr,
    <.br)
  })

}
