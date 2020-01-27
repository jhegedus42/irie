package app.server.httpServer.routes.fileUploading
import java.io.FileOutputStream
import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.util.ByteString
import io.circe.Encoder
import shared.dataStorage.model.{
  ImgHintToThisNotesText,
  ImgFileName,
  SizeInPercentage,
  SizeInPixel
}

import scala.concurrent.ExecutionContextExecutor

trait UploadFileRoute {

  implicit val system: ActorSystem

  implicit def executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  def route: Route = {

    path("user" / "upload" / "file") {
      (post & entity(as[Multipart.FormData])) { fileData =>
        complete {
          val fileName                 = UUID.randomUUID().toString
          val filePathWithoutExtension = "./images/"+fileName
          processFile(filePathWithoutExtension, fileData)
            .map { fileSize =>
              HttpResponse(
                StatusCodes.OK,
                entity = {

                  // get file size
                  import sys.process._
                  val width =
                    (s"""identify -format "%w" ${filePathWithoutExtension}""" !!).trim
                      .filterNot(_ == '"').toString.toDouble

                  val height =
                    (s"""identify -format "%h" ${filePathWithoutExtension}""" !!).trim
                      .filterNot(_ == '"').toString.toDouble

                  val encoding: String =
                    (s"""identify -format "%m" ${filePathWithoutExtension}""" !!).trim
                      .filterNot(_ == '"').toString.toLowerCase()

                  val newFileName = s"$fileName.$encoding"

                  s"""mv ${filePathWithoutExtension} ${filePathWithoutExtension}.${encoding}""" !!

                  val imgFileData = ImgHintToThisNotesText(
                    ImgFileName(newFileName),
                    SizeInPixel(width, height)
                  )

                  implicitly[Encoder[ImgHintToThisNotesText]]
                    .apply(imgFileData).spaces4

                }
              )
            }.recover {
              case ex: Exception =>
                HttpResponse(StatusCodes.InternalServerError,
                             entity = "Error in file uploading")
            }
        }
      }
    }
  }

  private def processFile(
    filePath: String,
    fileData: Multipart.FormData
  ) = {
    val fileOutput = new FileOutputStream(filePath)

    fileData.parts
      .mapAsync(1) { bodyPart ⇒
        def writeFileOnLocal(
          array:      Array[Byte],
          byteString: ByteString
        ): Array[Byte] = {
          val byteArray: Array[Byte] = byteString.toArray
          fileOutput.write(byteArray)
          array ++ byteArray
        }

        bodyPart.entity.dataBytes.runFold(Array[Byte]())(
          writeFileOnLocal
        )
      }.runFold(0)(_ + _.length)

  }

}
