package client._notRelevant.caching.cache.sodiumCache

import dataStorage.Value
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.{ExecutionContextExecutor, Future}

object AJAXModul {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  import io.circe.syntax._

  val headers: Map[String, String] = Map(
    "Content-Type" -> "application/json"
  )

//  def query(
//  )(
//    implicit decoder: Decoder[RT#Resp],
//    encoder:          Encoder[RT#Par]
//  ): Future[RT#Resp] = {
//      Ajax
//        .post(url, json_line, headers = headers)
//        .map(_.responseText)
//
//  }

}
