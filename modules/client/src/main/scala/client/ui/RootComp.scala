package client.ui

import client.cache.{Cache, CacheMap, NormalizedStateHolder}
import client.sodium.app.actions.SodiumActionWriteToConsole
import client.sodium.app.reactComponents.{
  STextArea,
  SodiumButton,
  SPreformattedText
}
import dataStorage.User
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor

object RootComp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val userCache: Cache[User] = NormalizedStateHolder.user

  def getComp = {
    val s = userCache.cell
      .updates().map((c: CacheMap[User]) => c.getPrettyPrintedString)

    val sc = SPreformattedText(s).comp

    val userName            = STextArea("init_text")
    val createNewUserButton = SodiumButton("Create New User")
    val text                = createNewUserButton.streamSink.snapshot(userName.cell)
    val writeToConsole      = SodiumActionWriteToConsole(text)
    // todo-now => create a user ...

    def render(s: String): VdomElement = {
      <.div("Hello there ",
            s,
            sc(),
            userName.vdom(),
            createNewUserButton.vdom())
    }

    val rootComp =
      ScalaComponent
        .builder[String]("Hello")
        .render_P(render)
        .build

    rootComp
  }

  // todo-now, add new user
  // SodiumAction - Insert New Entity

}
