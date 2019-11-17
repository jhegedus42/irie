package client.sodium.app.reactComponents.compositeComponents

import client.cache.{Cache, CacheMap, CacheProvider}
import client.sodium.app.actions.SActionWriteToConsole
import client.sodium.app.reactComponents.atomicComponents.{
  SButton,
  SPreformattedText,
  STextArea
}
import dataStorage.User
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

import scala.concurrent.ExecutionContextExecutor

case class NewUserCreator() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val userCache: Cache[User] = Cache.user

  def getComp = {
    val s = userCache.cell
      .updates().map((c: CacheMap[User]) => c.getPrettyPrintedString)

    val listOfUsers = SPreformattedText(s).comp

    val userName            = STextArea("init_text")
    val createNewUserButton = SButton("Create New User")
    val text                = createNewUserButton.getClick.snapshot(userName.cell)
    val writeToConsole      = SActionWriteToConsole(text)
    // todo-now => create a user ...

    def render: Unit => VdomElement = { _ =>
      <.div(
        listOfUsers(),
        userName.comp(),
        createNewUserButton.vdom()
      )
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp
  }

}
