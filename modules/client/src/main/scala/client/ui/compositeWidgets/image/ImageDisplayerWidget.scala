package client.ui.compositeWidgets.image

import java.net.FileNameMap

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


  def getImg(fileNameOpt:Option[String]) : VdomElement = {
    if(fileNameOpt.isDefined){
      val fn=fileNameOpt.head
      <.div(
        s"File name :$fn.",
        <.img(^.src := s"$fn", ^.alt:="image", ^.width:="100%"),
        <.br
      )
    }
    else{
      <.div("Image file has not been uploaded for this Image Entity yet.")
    }
  }

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
            getImg(x.fileName),
            <.hr,
            <.br
          )
        }
      }
    ).displayer


}