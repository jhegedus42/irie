package client.ui.compositeWidgets.specific.image.svg

//import japgolly.scalajs.react.vdom.svg_<^.{<, SvgTag, ^}
//import org.scalajs.dom.svg.Image
import japgolly.scalajs.react.vdom.{TagOf, svg_<^}
import shared.dataStorage.model.{
  ImgFileName,
  LocationInPercentage,
  LocationInPixel,
  SizeInPercentage,
  SizeInPixel,
  HintForNote
}
//import japgolly.scalajs.react.vdom.SvgTagOf
import japgolly.scalajs.react.vdom.svg_<^._
//import org.scalajs.dom.svg.{Image, SVG}
import japgolly.scalajs.react.vdom.SvgTags
import japgolly.scalajs.react.vdom.SvgTagOf
import japgolly.scalajs.react.vdom.SvgAttrs
import japgolly.scalajs.react.vdom.SvgAttrAndStyles
import org.scalajs.dom.{svg => *}
//import japgolly.scalajs.react.vdom.html_<^.{<, _}

object SVGElements {

  import org.scalajs.dom.{svg => *}
  final def clipPath = SvgTagOf[*.ClipPath]("clipPath")

  def translateAndScale(
    locationInPixel: LocationInPixel,
    scaleFactor:     Double
  )(children:        TagMod
  ) = {
    <.g(
      ^.transform :=
        s"translate(${locationInPixel.xInPixel} ${locationInPixel.yInPixel}) " +
        s"scale($scaleFactor)"
    )(
      children
    )
  }

  def svgElement(
    viewBox:  ViewBoxPX,
    location: LocationAndSizeInPixel
  )(children: TagMod
  ) = {
    <.svg(viewBox.getTag, location.getTags)(children)
  }

  case class ViewBoxPX(
    coordInPixel: LocationInPixel,
    sizeInPixel:  SizeInPixel) {

    def getTag =
      ^.viewBox := s"${coordInPixel.xInPixel} ${coordInPixel.yInPixel} ${sizeInPixel.width} ${sizeInPixel.height}"
  }

  case class LocationAndSizeInPixel(
    locationInPixel: LocationInPixel,
    sizeInPixel:     SizeInPixel) {

    def getTags =
      List(^.width := s"${sizeInPixel.width}",
           ^.height := s"${sizeInPixel.height}",
           ^.x := s"${locationInPixel.xInPixel}",
           ^.y := s"${locationInPixel.yInPixel}").toTagMod
  }

  def clipPathVal(
    l:  LocationAndSizeInPixel,
    id: String
  ) =
    <.defs(
      SVGElements.clipPath(^.id := "clipPath")(
        <.rect(l.getTags)
      )
    )

  def image(
             vh:              HintForNote,
             locationAndSize: LocationAndSizeInPixel
  ) = {
    svg_<^.<.image(
      ^.xlinkHref := vh.hint.fileName.fileNameAsString,
      locationAndSize.getTags
    )
  }

  def clippedImage(
    fn: ImgFileName,
    l:  LocationAndSizeInPixel
  ) = {
    <.image(^.xlinkHref := fn.fileNameAsString,
            l.getTags,
            ^.clipPath := "url(#clipPath)")
  }

}
