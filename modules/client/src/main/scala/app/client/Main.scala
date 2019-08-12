package app.client
import app.client.ui.components.router.RouterComp
import app.shared.utils.MacroExampleMakeHelloWorld
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element

import scala.concurrent.ExecutionContextExecutor
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

// this needs to be supressed
// we can have one such export
// otherwise ScalaJS fastOptJS fails
@JSExport( "Main" )
object Main extends js.JSApp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def setupLogging(): Unit = {}

  @JSExport
  def main(): Unit = {
    @MacroExampleMakeHelloWorld val x: String = "Welcome and"
    println( "hello sbt 1.2.8" )
    println( x )
    setupLogging()
    routedApp()
  }

  def routedApp(): Unit = {
//    AppCSS.load()
    val e: Element = document.getElementById( "rootComp" )
    println(
      s"Main.routedApp() : Router is just about to be mounted into a DIV."
    )
    RouterComp().router().renderIntoDOM( e )

  }

}
