package client.ui.compositeWidgets.specific.image

import java.net.FileNameMap

import client.cache.Cache
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.{CellOptionMonad, NoteOperations}
import client.sodium.core.Cell
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import shared.dataStorage.model.ImageWithQue
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class ImageDisplayerWidget(
  img: Cell[Option[ImageWithQue]]) {


  def getImg(fileNameOpt: String): VdomElement = {
      val fn = fileNameOpt.head
      <.div(
        s"File name :$fn.",
        <.img(^.src := s"$fn", ^.alt := "image", ^.width := "100%"),
        <.br
      )
  }



  lazy val imageDisplayer = {

    CellOptionDisplayerWidget[ImageWithQue](
      img, { x: ImageWithQue =>
        {
          <.div(
            <.hr,
            <.br,
            s"Image's title: ${x.title}",
            <.br,
            s"Image's file name :${x.fileName}",
            <.br,
            getImg(x.fileName),
            <.br
          )
        }
      }
    ).displayer
  }

}
