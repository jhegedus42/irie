package client.ui.compositeWidgets.specific.image

import java.net.FileNameMap

import client.cache.Cache
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.CellOptionMonad
import client.sodium.core.Cell
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import shared.dataStorage.model.HintForNote
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class ImageDisplayerWidget(img: Cell[Option[HintForNote]]) {

  def getImg(fileName: String): VdomElement = {
    val fn = fileName
    <.div(
      s"File name :$fn.",
      <.img(^.src := s"$fn", ^.alt := "image", ^.width := "100%"),
      <.br
    )
  }

  lazy val imageDisplayer = {

    CellOptionDisplayerWidget[HintForNote](
      img, { x: HintForNote =>
        {
          <.div(
            <.hr,
            <.br,
            s"Image's file name :${x.hint.fileName.fileNameAsString}",
            <.br,
            getImg(
              x.hint.fileName.fileNameWithPathAsString
            ),
            <.br
          )
        }
      }
    ).optDisplayer
  }

}
