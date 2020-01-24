package client.ui

import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C
import client.ui.compositeWidgets.specific.image.ImagesForANote
import client.ui.compositeWidgets.specific.image.svg.SVG
import client.ui.compositeWidgets.specific.note.NotesWidget
import client.ui.wrappedReact.{
  Crop,
  ImgCropWidget,
  ReactCrop,
  TagsInput
}
import japgolly.scalajs.react.vdom.SvgTagOf

object RootComp {

  import org.scalajs.dom.{svg => *}
  final def clipPath = SvgTagOf[*.ClipPath]("clipPath")

  lazy val svgDemo = {
//    import org.scalajs.dom.{svg => *}

    //http://tutorials.jenkov.com/svg/svg-viewport-view-box.html#viewport

    import japgolly.scalajs.react.vdom.svg_<^._

    val scale=0.5

//    val hintWidthPixels = ???
//    val hintHeightPixels = ???
//
//    val bgWidthPixels = ???
//    val bgHeightPixels = ???
//
//    val locInHintRelX = ???
//    val locInHintRelY = ???
//    val sizeInHintHeightRel = ???
//    val sizeInHintWidthRel = ???
//
//    val locInBGRelX = ???
//    val locInBGRelY = ???
//    val sizeInBGHeightRel = ???
//    val sizeInBGWidthRel = ???

    <.svg(^.viewBox := s"0 0 100 52", // 52 is the "aspect ratio", relative to 100
          ^.preserveAspectRatio := "xMinYMin meet",
          ^.width := s"100%",
          ^.height := s"100")(
      <.image(^.xlinkHref := SVG.backgroundExample, ^.width := s"100"),
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

//    <svg viewBox="0 0 100 100" preserveAspectRatio="xMinYMin meet" width="100%">
//      <image xlink:href="6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg" width="100"></image>
//      <svg width="100%" x="50" y="25" viewBox="10 10 100 100"><defs>
//        <clipPath id="clipPath">
//          <rect width="10%" x="10%" y="10%" height="15%">
//          </rect>
//        </clipPath>
//      </defs>
//        <image xlink:href="befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg" clip-path="url(#clipPath)" x="0" width="25%">
//        </image>
//      </svg>
//
//    </svg>

//    <svg viewBox="0 0 100 50" preserveAspectRatio="xMinYMin meet" width="100%">
//      <image xlink:href="6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg" width="100"></image>
//      <svg viewBox="10 14 100 50" width="100%" x="60" y="20"><defs>
//        <clipPath id="clipPath">
//          <rect width="10" height="10" x="10" y="14">
//          </rect>
//        </clipPath>
//      </defs>
//        <image xlink:href="befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg" width="25%" clip-path="url(#clipPath)" x="0">
//        </image>
//      </svg>
//    </svg>

//    <svg viewBox="0 0 100 52" preserveAspectRatio="xMinYMin meet" width="100%">
//      <image xlink:href="6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg" width="100"></image>
//      <svg viewBox="20 25 100 100" width="100%" height="103" x="50" y="25">
//        <defs>
//          <clipPath id="clipPath">
//            <rect width="20" height="25" x="20" y="25"></rect>
//          </clipPath>
//        </defs>
//        <image xlink:href="befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg" width="50" clip-path="url(#clipPath)" x="0"></image>
//      </svg>
//    </svg>

//    <svg viewBox="0 0 100 50" preserveAspectRatio="xMinYMin meet" width="200" height="100">
//
//      <image xlink:href="6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg" width="100"></image>
//      <svg viewBox="20 25 100 100" preserveAspectRatio="xMinYMin meet" width="100" height="100" x="50" y="25">
//        <defs>
//          <clipPath id="clipPath">
//            <rect width="20" height="25" x="20" y="25"></rect>
//          </clipPath>
//        </defs>
//        <image xlink:href="befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg" width="50" clip-path="url(#clipPath)" x="0"></image>
//
//      </svg>
//    </svg>
//
//    <svg viewBox="0 0 100 100">
//      <defs>
//        <clipPath id="clipPath">
//          <rect x="20" y="50" width="50" height="50"></rect>
//        </clipPath>
//      </defs>
//      <image xlink:href="6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg" width="100"></image>
//      <image xlink:href="befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg" clip-path="url(#clipPath)" width="100" x="-30">
//    </image>
//    </svg>
    // TODO-NOW :
    //  crop image with percentage

  }

  import japgolly.scalajs.react.ScalaComponent
  import japgolly.scalajs.react.vdom.html_<^.{<, _}
  import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      val nw = NotesWidget()
      <.div(
        svgDemo,
        <.main(C.container, ^.role := "container")(
          <.div(C.jumbotron)(
            nw.getComp(),
            <.br
          )
        )
      )
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp
  }
  // todo-now - ListOfNotes

}
