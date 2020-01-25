package client.ui.compositeWidgets.specific.image.svg

//import client.ui.RootComp.clipPath
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Json}
import japgolly.scalajs.react.vdom.SvgTagOf
import shared.dataStorage.model.CoordInPixel
//import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
//import japgolly.scalajs.react.vdom.svg_<^.{<, ^}
import shared.dataStorage.model.{ImgFileName, SizeInPixel, VisualHint}

case class SVG(
  background: VisualHint,
  hint:       VisualHint) {

  def asString: String = ???

}

object SVG {

  lazy val svgExample = {
    """
      |<svg width="100" height="150">
      |
      |  <circle cx="50" cy="50" r="40"
      |     stroke="green" stroke-width="4" fill="yellow"
      |  />
      |
      |  <circle cx="50" cy="70" r="40"
      |     stroke="green" stroke-width="4" fill="red"
      |  />
      |
      |Sorry, your browser does not support inline SVG.
      |
      |</svg>
      |""".stripMargin
  }

  lazy val svgDemo = {
    //    import org.scalajs.dom.{svg => *}

    //http://tutorials.jenkov.com/svg/svg-viewport-view-box.html#viewport

  }

  lazy val jsonVisualHint2 = {
    s"""
       |{
       |    "title" : "csaj padon",
       |    "fileName" : {
       |        "fileNameAsString" : "./befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg"
       |    },
       |    "hintToThisImage" : {
       |        "rect" : {
       |            "center" : {
       |                "x" : 151,
       |                "y" : 221
       |            },
       |            "size" : {
       |                "width" : 209,
       |                "height" : 217
       |            }
       |        }
       |    },
       |    "placeForHintToNextImage" : {
       |        "rect" : {
       |            "center" : {
       |                "x" : 28,
       |                "y" : 508
       |            },
       |            "size" : {
       |                "width" : 167,
       |                "height" : 125
       |            }
       |        }
       |    }
       |}
       |""".stripMargin
  }

  lazy val jsonVisualHint =
    s"""
       |{
       |    "title" : "default image title",
       |    "fileName" : {
       |        "fileNameAsString" : "./43ea782f-6960-44c4-a0d3-5227917abef7.jpeg"
       |    },
       |    "hintToThisImage" : {
       |        "rect" : {
       |            "center" : {
       |                "x" : 453,
       |                "y" : 159
       |            },
       |            "size" : {
       |                "width" : 422,
       |                "height" : 302
       |            }
       |        }
       |    },
       |    "placeForHintToNextImage" : {
       |        "rect" : {
       |            "center" : {
       |                "x" : 769.6954819088226,
       |                "y" : 114.14639190479977
       |            },
       |            "size" : {
       |                "width" : 231.87857971712867,
       |                "height" : 228.04066253120334
       |            }
       |        }
       |    }
       |}
       |""".stripMargin

  lazy val visualHint1: VisualHint = {
    jsonVisualHintParser(jsonVisualHint)
  }

  lazy val visualHint2: VisualHint = {
    jsonVisualHintParser(jsonVisualHint2)
  }

  def jsonVisualHintParser(
    visualHintAsJSON: String
  )(
    implicit
    vh: Decoder[VisualHint]
  ) = {

    lazy val s = vh.decodeJson(Json.fromString(visualHintAsJSON))
    s.toTry.toOption.head
  }

  import org.scalajs.dom.{svg => *}
  final def clipPath = SvgTagOf[*.ClipPath]("clipPath")

  val scale = 0.5
  import japgolly.scalajs.react.vdom.svg_<^._

  //    vh1:VisualHint

  def getImgSize(vh: VisualHint): SizeInPixel = {
    ???
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
      List(^.width := s"100",
           ^.height := s"100",
           ^.x := s"50",
           ^.y := s"25").toTagMod
  }

  case class SVG(
    viewBox:  ViewBox,
    location: Location,
    children: SvgTag) {

    def getVSVG = {
      <.svg(
        <.svg(viewBox.getTag, location.getTags)(children)
      )
    }
  }

  def f(
    vh1: VisualHint,
    vh2: VisualHint
  ) = {

    val clipPathVal =
      <.svg(^.viewBox := s"20 25 100 100",
            ^.width := s"100",
            ^.height := s"100",
            ^.x := s"50",
            ^.y := s"25")(
        <.defs(
          clipPath(^.id := "clipPath")(
            <.rect(^.width := s"20",
                   ^.height := s"25",
                   ^.x := s"20",
                   ^.y := s"25")
          )
        )
      )

    <.svg(
      ^.viewBox := s"0 0 100 52", // 52 is the "aspect ratio", relative to 100
      ^.preserveAspectRatio := "xMinYMin meet",
      ^.width := s"100%",
      ^.height := s"100"
    )(
//      <.image(^.xlinkHref := SVG.backgroundExample,
//              ^.width := s"100"),
//      clipPath,
//      <.image(^.xlinkHref := SVG.hintExample,
//              ^.width := s"50",
//              ^.clipPath := "url(#clipPath)",
//              ^.x := "0")
    )

  }

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

  lazy val backgroundExample =
    "6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg"
  lazy val hintExample = "befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg"

}
