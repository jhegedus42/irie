package client.ui.compositeWidgets.specific.image.svg

//import japgolly.scalajs.react.vdom.svg_<^.{<, SvgTag, ^}
//import org.scalajs.dom.svg.Image
import japgolly.scalajs.react.vdom.{TagOf, svg_<^}
import shared.dataStorage.model.{
  CoordInPixel,
  ImgFileName,
  SizeInPixel,
  VisualHint
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

  def svgElement(
    viewBox:  ViewBox,
    location: Location
  )(children: TagMod
  ) = {
    <.svg(viewBox.getTag, location.getTags)(children)
  }

  case class ViewBox(
    c: CoordInPixel,
    s: SizeInPixel) {
    def getTag = ^.viewBox := s"${c.x} ${c.y} ${s.width} ${s.height}"
  }

  case class Location(
    loc:  CoordInPixel,
    size: SizeInPixel) {

    def getTags =
      List(^.width := s"${size.width}",
           ^.height := s"${size.height}",
           ^.x := s"${loc.x}",
           ^.y := s"${loc.y}").toTagMod
  }

  def clipPathVal(
    l:  Location,
    id: String
  ) =
    <.defs(
      SVGElements.clipPath(^.id := "clipPath")(
        <.rect(l.getTags)
      )
    )

  def image(
    vh: VisualHint,
    l:  Location
  ) = {
    svg_<^.<.image(^.xlinkHref := vh.fileName.fileNameAsString,
                   l.getTags )
  }

  def clippedImage(
    fn: ImgFileName,
    l:  Location
  ) = {
    <.image(^.xlinkHref := fn.fileNameAsString,
            l.getTags,
            ^.clipPath := "url(#clipPath)")
  }

}
