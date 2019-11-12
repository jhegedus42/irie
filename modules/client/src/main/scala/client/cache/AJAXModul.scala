package client.cache

import scala.concurrent.ExecutionContextExecutor

object AJAXModul {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  import io.circe.syntax._

  val headers: Map[String, String] = Map(
    "Content-Type" -> "application/json"
  )


}

