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

// https://github.com/scalajs-io/express-fileupload

// https://stackoverflow.com/questions/44357501/how-to-upload-a-file-using-ajax-in-scalajs/47630262

// https://gitlab.com/bullbytes/scala-js-example

// https://gitter.im/scala-js/scala-js?at=59db182f177fb9fe7e55c63f

// https://developer.mozilla.org/en-US/docs/Web/API/FormData

// https://thoughtbot.com/blog/ridiculously-simple-ajax-uploads-with-formdata

trait UploadFileRoute {

  implicit val system: ActorSystem

  implicit def executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  def route: Route = {
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
                  s"$filePath"
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
