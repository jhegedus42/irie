package client.ui.compositeWidgets.image
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^, _}
import japgolly.scalajs.react._

import scala.concurrent.{ExecutionContextExecutor, Future}
import bootstrap4.TB.C
import bootstrap4.TB.C
import client.cache.{Cache, UpdateEntityInCacheCmd}
import client.sodium.core.Cell
import client.ui.compositeWidgets.note.NotesWidget
import org.scalajs.dom.FormData
import org.scalajs.dom._
import shared.dataStorage.{Image, TypedReferencedValue}

import scala.scalajs.js
import scala.util.{Failure, Success, Try}
//import scala.scalajs.js.

import js.Dynamic.{global => g, newInstance => jsnew}

import scalajs.runtime.propertiesOf
import scalajs.js._

case class ImageUploaderWidget(
  imgOpt: Cell[Option[TypedReferencedValue[Image]]],
  c:      Cache[Image]) {

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
            formData.append("file", file)
            val xhr = new XMLHttpRequest()

            val url = "/user/upload/file"

            import org.scalajs.dom.ext.Ajax
            val f: Future[XMLHttpRequest] = Ajax.post(url, formData)
            implicit def executionContext: ExecutionContextExecutor =
              scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

            f.map(_.responseText).onComplete(
                (res: Try[String]) =>
                  res match {
                    case Failure(exception) => {
                      println(s"Failure: $exception")
                    }
                    case Success(value) => {
                      println(s"Success $value")
                      // todo now, send image update with new file-name
                      val s = imgOpt.sample()
                      if (s.isDefined) {
                        val img = s.head
                        val v =
                          img.versionedEntityValue.valueWithoutVersion
                        import monocle.macros.syntax.lens._
                        val newVal =
                          v.lens(_.fileName).set(Some(value))
                        lazy val cmd =
                          UpdateEntityInCacheCmd(img, newVal)
                        c.updateEntityCommandStream.send(cmd)
                      }

                    }
                  }
              )

          }
        }
      )
    )
  }

}

