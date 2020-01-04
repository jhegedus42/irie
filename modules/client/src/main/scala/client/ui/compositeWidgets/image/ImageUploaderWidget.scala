package client.ui.compositeWidgets.image
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^, _}
import japgolly.scalajs.react._

import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C
import bootstrap4.TB.C
import client.ui.compositeWidgets.note.NotesWidget
import org.scalajs.dom.FormData
import org.scalajs.dom._

import scala.scalajs.js
//import scala.scalajs.js.

import js.Dynamic.{global => g, newInstance => jsnew}

import scalajs.runtime.propertiesOf
import scalajs.js._

case class ImageUploaderWidget() {

  def render: () => VdomElement = { () =>
    <.div(
      <.h2("Image Uploader"),
      <.input(^.id := "the-file",
              ^.name := "file",
              ^.`type` := "file"),
      <.button(
        "Submit",
        ^.onClick --> {
          Callback {
            val fileInput = g.document.getElementById("the-file")

            val fileInputD: js.Dynamic = fileInput

            val files: js.Array[Dynamic] =
              fileInputD.files.asInstanceOf[js.Array[Dynamic]]

            val file = files(0)

            val formData = new FormData()
            formData.append("file",file)
            val xhr = new XMLHttpRequest()
            xhr.open("POST","/user/upload/file")
            xhr.send(formData)


          }
        }
      )
    )
  }

  def handleSubmit(): Unit = {
//    val fd= new FormData()
//    val
//    val fileInput: Element = document.getElementById("the-file")
//    val file: Array[String] =propertiesOf(fileInput)
//    val fileInputAttr: Attr = fileInput.getAttributeNode("files")
//    fd.append("file",)
  }

}
