package client.ui

import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C
import client.ui.compositeWidgets.specific.image.ImagesForANote
import client.ui.compositeWidgets.specific.image.svg.SVG
import client.ui.compositeWidgets.specific.note.NotesWidget
import client.ui.wrappedReact.{Crop, ImgCropWidget, ReactCrop, TagsInput}
import japgolly.scalajs.react.vdom.SvgTagOf

object RootComp {

  import org.scalajs.dom.{svg => *}
  final def clipPath = SvgTagOf[*.ClipPath]("clipPath")


  lazy val svgDemo = {
//    import org.scalajs.dom.{svg => *}

    //http://tutorials.jenkov.com/svg/svg-viewport-view-box.html#viewport

    import japgolly.scalajs.react.vdom.svg_<^._
    <.svg(^.viewBox:="0 0 100 100", ^.preserveAspectRatio:="xMidYMid meet",^.width:="100%")(
      <.defs(
        clipPath(^.id:="clipPath")(
         <.rect(^.width:="50",^.height:="50",^.x:="25",^.y:="25")
        )
      ),
        <.image(^.xlinkHref := SVG.backgroundExample, ^.width:="100" ),
        <.image(^.xlinkHref := SVG.hintExample,
                ^.width := "100", ^.height:="100",  ^.clipPath:="url(#clipPath)", ^.x:="0")

      )
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
