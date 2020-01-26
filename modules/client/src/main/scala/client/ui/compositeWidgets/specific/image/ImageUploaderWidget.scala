package client.ui.compositeWidgets.specific.image
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^, _}
import japgolly.scalajs.react._

import scala.concurrent.{ExecutionContextExecutor, Future}
import bootstrap4.TB.C
import bootstrap4.TB.C
import client.cache.Cache
import client.cache.commands.UpdateEntityInCacheCmd
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.sodium.core.Cell
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import client.ui.compositeWidgets.specific.note.NotesWidget
import io.circe.Decoder
import org.scalajs.dom.FormData
import org.scalajs.dom._
import shared.dataStorage.model.{ImgHintToThisNotesText, Note, VisualHint}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

import scala.scalajs.js
import scala.util.{Failure, Success, Try}
//import scala.scalajs.js.

import js.Dynamic.{global => g, newInstance => jsnew}

import scalajs.runtime.propertiesOf
import scalajs.js._

case class ImageUploaderWidget(
  selectedNote: CellOption[TypedReferencedValue[Note]]) {

  lazy val comp =
    CellOptionDisplayerWidget(selectedNote.co, render(_))

  def render(noteTRV: TypedReferencedValue[Note]): VdomElement = {
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
                      val decoder = implicitly[Decoder[ImgHintToThisNotesText]]
                      import io.circe._
                      import io.circe.generic.JsonCodec
                      import io.circe.generic.auto._
                      import io.circe.parser._
                      import io.circe.syntax._

                      val res: Either[Error, ImgHintToThisNotesText] =
                        decode[ImgHintToThisNotesText](value)

                      if (res.toOption.isDefined) {
                        val imgDataNew=res.toOption.head

                        // todo now, send image update with new file-name
                        val noteOpt = selectedNote.co.sample()
                        if (noteOpt.isDefined) {
                          val note =
                            noteOpt.head.versionedEntityValue.valueWithoutVersion
                          import monocle.macros.syntax.lens._
                          val newVal =
                            note
                              .lens(_.visualHint.imgHintToThisNotesText).set(
                              imgDataNew
                            )

                          // todo - get image size

                          lazy val cmd =
                            UpdateEntityInCacheCmd(noteOpt.head, newVal)
                          Cache.noteCache.updateEntityCommandStream
                            .send(cmd)

                      } else {
                          println(s"img upload failed, result is $value")
                        }
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
