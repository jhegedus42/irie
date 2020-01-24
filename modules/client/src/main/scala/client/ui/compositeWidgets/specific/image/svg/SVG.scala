package client.ui.compositeWidgets.specific.image.svg

import client.ui.RootComp.clipPath
import japgolly.scalajs.react.vdom.svg_<^.{<, ^}
import shared.dataStorage.model.{ImgFileName, SizeInPixel, VisualHint}

case class SVG(
  background: VisualHint,
  hint:       VisualHint) {

  def asString: String = ???

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

  import japgolly.scalajs.react.vdom.svg_<^._

  import japgolly.scalajs.react.vdom.svg_<^._

  val scale = 0.5

  //    vh1:VisualHint

  def getImgSize(fn: ImgFileName): SizeInPixel = {
    ???
  }

  def f(
    vh1: VisualHint,
    vh2: VisualHint
  ) = {
    <.svg(
      ^.viewBox := s"0 0 100 52", // 52 is the "aspect ratio", relative to 100
      ^.preserveAspectRatio := "xMinYMin meet",
      ^.width := s"100%",
      ^.height := s"100"
    )(
      <.image(^.xlinkHref := SVG.backgroundExample,
              ^.width := s"100"),
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
        ),
        <.image(^.xlinkHref := SVG.hintExample,
                ^.width := s"50",
                ^.clipPath := "url(#clipPath)",
                ^.x := "0")
      )
    )
  }

  lazy val answer = <.svg(^.width := "600", ^.height := "400")(
    <.image(^.xlinkHref := SVG.backgroundExample, ^.width := "300"),
    <.image(^.xlinkHref := SVG.hintExample,
            ^.width := "30",
            ^.height := "20",
            ^.transform := "translate(100,100)")

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
  )
}

object SVG {

  lazy val backgroundExample =
    "6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg"
  lazy val hintExample = "befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg"

}
