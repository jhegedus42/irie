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
    <.svg(^.width := "100%",^.height:="200")(
        <.image(^.xlinkHref := SVG.backgroundExample,
                ^.width := "100%" ),
      <.svg(^.width:="25%",
        ^.x:="50%",
        ^.y:="25%",
        <.image(^.xlinkHref := SVG.hintExample,
                ^.width := "100%"
                )

      )


      // TODO-NOW :
      //  crop image with percentage

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
