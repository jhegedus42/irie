package app.client
import app.client.ui.css.AppCSS
import app.client.ui.routing.RouterComp
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import slogging.{LazyLogging, _}

import scala.concurrent.ExecutionContextExecutor
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

// this needs to be supressed
// we can have one such export
// otherwise ScalaJS fastOptJS fails
@JSExport( "Main" )
object Main extends js.JSApp with LazyLogging{

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


  def setupLogging(): Unit = {
    LoggerConfig.factory = ConsoleLoggerFactory()
    LoggerConfig.level   = LogLevel.TRACE


  }

  @JSExport
  def main(): Unit = {
    setupLogging()
    routedApp()
  }

  def routedApp(): Unit = {
    AppCSS.load
    val e: Element = document.getElementById( "rootComp" )
    logger.trace("Router is just about to be mounted into a DIV.")
    RouterComp("kess").router().renderIntoDOM(e)

  }

}



