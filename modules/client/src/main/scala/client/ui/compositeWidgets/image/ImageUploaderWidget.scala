package client.ui.compositeWidgets.image
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C

import bootstrap4.TB.C
import client.ui.compositeWidgets.note.NotesWidget
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^}

case class ImageUploaderWidget(){


  def render: Unit => VdomElement = { _ =>
    <.div(
      <.main(C.container, ^.role := "container")(
        <.div(C.jumbotron)(
          <.h1("Image Uploader"),
          <.input(^.id := "the-file",
            ^.name := "file",
            ^.`type` := "file"),
          <.input(^.id := "submit-btn",
            ^.value := "Upload",
            ^.`type` := "submit")
        )
      )
    )
  }


}
