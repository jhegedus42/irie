package client.ui.compositeWidgets.specific.image.svg

//import client.ui.RootComp.clipPath
import client.ui.compositeWidgets.specific.image.svg.CompositeSVGDisplayer.VisualLinkData
import client.ui.compositeWidgets.specific.image.svg.SVGElements._
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Json}
import japgolly.scalajs.react.vdom.{SvgTagOf, svg_<^}
import org.scalajs.dom.svg.SVG
import shared.dataStorage.model.{
  LocationInPercentage,
  LocationInPixel,
  SizeInPixel
}
//import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import japgolly.scalajs.react.vdom.svg_<^.{<, ^}
import shared.dataStorage.model.{
  ImgFileName,
  SizeInPercentage,
  HintForNote
}

object VisualLinkAsSVGHelpers {

  import japgolly.scalajs.react.vdom.svg_<^._

  def visualLinkToNextNoteAsSVG(vld: VisualLinkData) = {
    lazy val hintForThis = vld.hintForThis
    lazy val imgSizeForThis =
      hintForThis.hint.sizeInPixel

    lazy val origo     = LocationInPixel(0, 0)
    lazy val viewBoxPX = ViewBoxPX(origo, imgSizeForThis)

    lazy val translateTo: LocationInPixel =
      vld.hintForThis.rectForTail.rect.upperLeftCornerXYInPercentage
        .toLocationInPixel(imgSizeForThis)

    lazy val imgSizeForNext =
      vld.hintForNext.hint.sizeInPixel

    lazy val sizeOfHintToNextInPixel: SizeInPixel =
      vld.hintForNext.rectForHead.rect.sizeInPercentage
        .toSizeInPixel(imgSizeForNext)

    lazy val sizeOfTargetRectInPixel: SizeInPixel =
      vld.hintForThis.rectForTail.rect.sizeInPercentage
        .toSizeInPixel(imgSizeForThis)

    lazy val scaleFactor = sizeOfTargetRectInPixel.width /
      sizeOfHintToNextInPixel.width


    <.svg(viewBoxPX.getTag)(
      hintToThisNoteAsSVG(vld.hintForThis),
      translateAndScale(translateTo, scaleFactor)(
        hintToNextNoteAsSVG(vld.hintForNext)
      )
    )

  }

//  <svg viewBox="0 0 1504 776">
//    <svg viewBox="0 0 1504 776"><image xlink:href="1402586f-1a70-4258-8a44-10f9b3cdd534.jpeg" width="1504" height="776" x="0" y="0"></image></svg>
//    <g transform="translate( 1100 200 ) scale( 2 )">
//      <svg viewBox="0 0 151.40000000000003 137.6" width="151.40000000000003" height="137.6" x="0" y="0"><image xlink:href="4319f91a-a8df-472f-9640-4a05b07961ec.jpeg" width="362" height="578" x="-182.00000000000003" y="-20"></image>
//      </svg>
//    </g>
//  </svg>

  def hintToThisNoteAsSVG(hint: HintForNote) = {
    lazy val imgSizePixel = hint.hint.sizeInPixel

    lazy val origo     = LocationInPixel(0, 0)
    lazy val viewBoxPX = ViewBoxPX(origo, imgSizePixel)

    lazy val imgLoc =
      LocationAndSizeInPixel(origo, imgSizePixel)

    lazy val img = image(hint, imgLoc)

    <.svg(viewBoxPX.getTag)(img)
  }

  def hintToNextNoteAsSVG(hint: HintForNote): VdomTagOf[SVG] = {

    lazy val imgSizePixel = hint.hint.sizeInPixel

    lazy val hintLocPerc: LocationInPercentage =
      hint.rectForHead.rect.upperLeftCornerXYInPercentage

    lazy val hintLocPixel: LocationInPixel =
      hintLocPerc.toLocationInPixel(imgSizePixel)

    lazy val hintSizePerc: SizeInPercentage =
      hint.rectForHead.rect.sizeInPercentage

    lazy val hintSizeInPixel: SizeInPixel = {
      hintSizePerc.toSizeInPixel(imgSizePixel)
    }

    lazy val viewBoxPX =
      ViewBoxPX(LocationInPixel(0, 0), hintSizeInPixel)

    lazy val svgLoc =
      LocationAndSizeInPixel(LocationInPixel(0, 0), hintSizeInPixel)

    lazy val imgLoc =
      LocationAndSizeInPixel(
        LocationInPixel(-hintLocPixel.xInPixel,
                        -hintLocPixel.yInPixel),
        imgSizePixel
      )

    lazy val img =
      image(hint, imgLoc)

    lazy val res: VdomTagOf[SVG] = svgElement(viewBoxPX, svgLoc)(img)
//    res
    res
  }

}
