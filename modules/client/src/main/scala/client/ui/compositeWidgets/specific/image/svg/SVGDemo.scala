package client.ui.compositeWidgets.specific.image.svg

//import client.ui.RootComp.clipPath
import client.ui.compositeWidgets.specific.image.svg.SVGElements._
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Json}
import japgolly.scalajs.react.vdom.{SvgTagOf, svg_<^}
import org.scalajs.dom.svg.SVG
import shared.dataStorage.model.CoordInPixel
//import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import japgolly.scalajs.react.vdom.svg_<^.{<, ^}
import shared.dataStorage.model.{ImgFileName, SizeInPixel, VisualHint}

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

  lazy val imgInSVGWithViewBox  : VdomTagOf[SVG] ={
    lazy val vb=ViewBox(CoordInPixel(0, 0), SizeInPixel(100, 150))
    lazy val svgLoc= Location(CoordInPixel(0,0),SizeInPixel(400,400))

    lazy val imgLoc= Location(CoordInPixel(0,0),SizeInPixel(100,200))
    lazy val img=image(VisualHintDemoData.visualHint2,imgLoc)
    lazy val res= svgElement(vb,svgLoc)(img)
//    res
    res
  }

}

