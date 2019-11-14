package client.cache

import scala.concurrent.ExecutionContextExecutor

object AJAXModul {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  import io.circe.syntax._



}

