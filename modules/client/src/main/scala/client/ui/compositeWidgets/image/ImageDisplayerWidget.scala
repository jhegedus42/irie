package client.ui.compositeWidgets.image

import client.cache.Cache
import client.sodium.core.Cell
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import shared.dataStorage.{Image, TypedReferencedValue, Value}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

case class ImageDisplayerWidget(
  img: Cell[Option[TypedReferencedValue[Image]]]
) {

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
            s"Image's file name :${x.fileName}",
            <.br,
            <.hr,
            <.br
          )
        }
      }
    ).displayer


}
