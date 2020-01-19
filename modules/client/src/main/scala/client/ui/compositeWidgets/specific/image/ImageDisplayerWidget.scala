package client.ui.compositeWidgets.specific.image

import java.net.FileNameMap

import client.cache.Cache
import client.cache.relationalOperations.{
  CellOptionMonad,
  NoteOperations
}
import client.sodium.core.Cell
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import shared.dataStorage.{
  ImageWithQue,
  Note,
  TypedReferencedValue,
  Value
}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}

case class ImageDisplayerWidget(
  img: Cell[Option[TypedReferencedValue[ImageWithQue]]]) {

  lazy val cellOptionImage: Cell[Option[ImageWithQue]] =
    img.map(_.map(_.versionedEntityValue.valueWithoutVersion))

  def getImg(fileNameOpt: Option[String]): VdomElement = {
    if (fileNameOpt.isDefined) {
      val fn = fileNameOpt.head
      <.div(
        s"File name :$fn.",
        <.img(^.src := s"$fn", ^.alt := "image", ^.width := "100%"),
        <.br
      )
    } else {
      <.div(
        "Image file has not been uploaded for this Image Entity yet."
      )
    }
  }

  lazy val notesBelongingToThisImage = {

    lazy val res
      : CellOptionMonad.CellOption[Set[TypedReferencedValue[Note]]] =
      NoteOperations.getNotesForAnImage(???)

    CellOptionDisplayerWidget[Set[TypedReferencedValue[Note]]](
      res.co, { x: Set[TypedReferencedValue[Note]] =>


        <.div(
          <.hr,
          <.br,
          "The following notes refer to this Image:",

          // todo - write a list displayer

          <.br,
          <.br,
          <.hr
        )
      }
    )
  }

  lazy val imageDisplayer = {

    CellOptionDisplayerWidget[ImageWithQue](
      cellOptionImage, { x: ImageWithQue =>
        {
          <.div(
            <.hr,
            <.br,
            s"Image's title: ${x.title}",
            <.br,
            s"Image's file name :${x.fileName}",
            <.br,
            s"It belongs to the following Note:",
            <.br,
            s"${x.referenceToNote}",
            getImg(x.fileName),
            <.br
          )
        }
      }
    ).displayer
  }

}
