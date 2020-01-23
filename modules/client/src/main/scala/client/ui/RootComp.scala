package client.ui


import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C
import client.ui.compositeWidgets.specific.image.ImagesForANote
import client.ui.compositeWidgets.specific.note.NotesWidget
import client.ui.wrappedReact.{
  Crop,
  ReactCrop,
  ImgCropWidget,
  TagsInput
}

object RootComp {

  lazy val svgDemo = {
//    import org.scalajs.dom.{svg => *}
    import japgolly.scalajs.react.vdom.svg_<^._
    <.svg(
      <.circle(^.cx := "50",
               ^.cy := "50",
               ^.r := "50",
               ^.fill := "red")
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
