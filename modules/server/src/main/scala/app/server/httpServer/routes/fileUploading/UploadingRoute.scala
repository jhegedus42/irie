package app.server.httpServer.routes.fileUploading
import java.io.FileOutputStream
import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.util.ByteString

import scala.concurrent.ExecutionContextExecutor

// from https://github.com/knoldus/akka-http-file-upload/blob/master/src/main/scala/com/rishi/Boot.scala

trait UploadingRoute {

  implicit val system: ActorSystem

  implicit def executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  def uploadFile: Route = {
    path("user" / "upload" / "file") {
      (post & entity(as[Multipart.FormData])) { fileData =>
        complete {
          val fileName = UUID.randomUUID().toString
          val temp     = System.getProperty("java.io.tmpdir")
          val filePath = temp + "/" + fileName
          processFile(filePath, fileData)
            .map { fileSize =>
              HttpResponse(
                StatusCodes.OK,
                entity =
                  s"File successfully uploaded. Fil size is $fileSize"
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
