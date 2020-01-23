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

object RootComp {

  lazy val svgDemo = {
//    import org.scalajs.dom.{svg => *}
    import japgolly.scalajs.react.vdom.svg_<^._
    <.svg(^.width:="600",^.height:="400")(
      <.circle(^.cx := "50",
               ^.cy := "50",
               ^.r := "50",
               ^.fill := "red"),
      <.image(^.xlinkHref := SVG.backgroundExample,
              ^.width := "100%",
              ^.height := "100%"),

      <.image(^.xlinkHref := SVG.hintExample,
        ^.width := "10%",
        ^.height := "10%",^.transform:="translate(100,100)")

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

  import japgolly.scalajs.react.ScalaComponent
  import japgolly.scalajs.react.vdom.html_<^.{<, _}
  import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      val nw = NotesWidget()
      <.div(
        <.main(C.container, ^.role := "container")(
          <.div(C.jumbotron)(
            nw.getComp(),
            <.br,
            svgDemo
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
