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
import shared.dataStorage.model.{ImgFileName, SizeInPixel, VisualHint}

object RootComp {

  import japgolly.scalajs.react.ScalaComponent
  import japgolly.scalajs.react.vdom.html_<^.{<, _}
  import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      val nw = NotesWidget()
      <.div(
//        svgDemo,
        <.main(C.container, ^.role := "container")(
          <.div(C.jumbotron)(
            nw.getComp(),
            <.br,
            <.hr,
            <.h2("static visual hint json decoded example"),
            <.br,
            <.pre(SVG.jsonVisualHint)
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
