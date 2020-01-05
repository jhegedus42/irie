package client.ui.compositeWidgets.image

import client.cache.Cache
import client.sodium.core.Cell
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import shared.dataStorage.{Image, TypedReferencedValue, Value}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

case class ImageDisplayerWidget[V <: Value[V]](
  img: Cell[Option[TypedReferencedValue[Image]]]
)(
  implicit
  cache: Cache[V]) {

  lazy val cellOptionImage: Cell[Option[Image]] =
    img.map(_.map(_.versionedEntityValue.valueWithoutVersion))


  lazy val imageDisplayer =
    CellOptionDisplayerWidget[Image](
      cellOptionImage, { x: Image =>
        {
          <.div(
            <.hr,
            <.br,
            s"Image's title: ${x.title}",
            <.br,
            <.hr,
            <.br
          )
        }
      }
    ).displayer


}
