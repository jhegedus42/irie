package client.ui.compositeWidgets.specific.image.svg

//import client.ui.RootComp.clipPath
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
  VisualHint
}

case class SVGDemo(
  background: VisualHint,
  hint:       VisualHint) {

  def asString: String = ???

}

object SVGDemo {

  val scale = 0.5
  import japgolly.scalajs.react.vdom.svg_<^._

//  def f(
//    vh1: VisualHint,
//    vh2: VisualHint
//  ) = {
//
//
//
//  }

//  lazy val answer = <.svg(^.width := "600", ^.height := "400")(
//    <.image(^.xlinkHref := SVG.backgroundExample, ^.width := "300"),
//    <.image(^.xlinkHref := SVG.hintExample,
//            ^.width := "30",
//            ^.height := "20",
//            ^.transform := "translate(100,100)")
//
  // FIX-BUG :
  //  I need to click 2 times on a note so that the
  //  proper placement in the cropper is updated.
  //  => WHERE IS THE BUG ???

  // todo-now:
  //  clip

  // TODO-NOW :
  //  translate image
  //  crop image
  //
//  )

//  lazy val backgroundExample =
//    "6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg"
//  lazy val hintExample = "befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg"

  // todo now
  //  put the result of imgInSVGWithViewBox into
  //  the image to which this hint is pointing

  def imgInSVGWithViewBox(hint: VisualHint): VdomTagOf[SVG] = {

    lazy val imgSizePixel = hint.fileData.sizeInPixel

    lazy val hintLocPerc: LocationInPercentage =
      hint.hintToThisImage.rect.upperLeftCornerXYInPercentage

    lazy val hintLocPixel: LocationInPixel =
      hintLocPerc.toLocationInPixel(imgSizePixel)

    lazy val hintSizePerc: SizeInPercentage =
      hint.hintToThisImage.rect.sizeInPercentage

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

    lazy val res = svgElement(viewBoxPX, svgLoc)(img)
//    res
    res
  }

}
