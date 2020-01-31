package client.ui.compositeWidgets.specific.image

import java.net.FileNameMap

import client.cache.Cache
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.CellOptionMonad
import client.sodium.core.Cell
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import shared.dataStorage.model.{HintForNote, Note}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class ImageDisplayerWidget(note: CellOption[Note]) {

  def getImg(fileName: String): VdomElement = {
    val fn = fileName
    <.div(
      s"File name :$fn.",
      <.img(^.src := s"$fn", ^.alt := "image", ^.width := "100%"),
      <.br
    )
  }

  val imageDisplayer = {

    CellOptionDisplayerWidget[Note](
      note.co, { x: Note =>
        {
          <.div(
            <.hr,
            <.br,
            s"Image's file name :${x.visualHint.hint.fileName.fileNameAsString}",
            <.br,
            getImg(
              x.visualHint.hint.fileName.fileNameWithPathAsString
            ),
            <.br
          )
        }
      }
    ).optDisplayer
  }

}

object ImageDisplayerWidget {}
