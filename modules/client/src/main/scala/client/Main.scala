package client

import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.Element
import sodium.components.SodiumButton

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Try

@JSExport("Main")
object Main extends js.JSApp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  val button = SodiumButton("hello")

  @JSExport
  def main(): Unit = {
    val e: Element = document.getElementById("rootComp")
    button.getVDOM().renderIntoDOM(e)
  }

  def makeTestRequest(): Unit = TestAjaxRequest.query()

  button.streamSink.listen((x: Unit) => makeTestRequest())

}

object TestAjaxRequest {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  import io.circe.syntax._

  val headers: Map[String, String] = Map(
    "Content-Type" -> "application/json"
  )

  val json =
    """
      |{
      |    "par" : {
      |        "uuid" : "53e69a57-5da3-44a7-bb67-e98752ca9a9c"
      |    },
      |    "res" : null
      |}
      |""".stripMargin

  def query()= {
    Ajax
      .post("http://localhost:8080/GetAllEntityiesForUser",
            json,
            headers = headers)
      .map(_.responseText).onComplete((x: Try[String]) =>println(x))
  }

}
