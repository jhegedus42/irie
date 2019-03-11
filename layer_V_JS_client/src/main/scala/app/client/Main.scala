package app.client
import app.shared.data.model.LineText
import app.shared.data.ref.Ref
import app.testHelpersShared.data.TestEntities
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers.SetTimeoutHandle
import app.client.comm.REST
import app.client.ui.components.rootComponents.cacheTestRootComp.{CacheTestRootComp, CacheTestRootCompProps}
import app.client.ui.css.AppCSS
import app.client.ui.routing.RouterComp
import app.shared.rest.routes.crudRequests.GetEntityRequest
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element

import scala.concurrent.ExecutionContextExecutor
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import io.circe
import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.parser.decode
import slogging.LazyLogging
import slogging._

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



