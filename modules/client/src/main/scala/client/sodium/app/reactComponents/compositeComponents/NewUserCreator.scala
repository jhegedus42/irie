package client.sodium.app.reactComponents.compositeComponents

import client.cache.{Cache, CacheMap, CacheProvider}
import client.sodium.app.actions.SActionWriteToConsole
import client.sodium.app.reactComponents.atomicComponents.{
  CellTemplate,
  SButton,
  SPreformattedText,
  STextArea
}
import client.sodium.core
import dataStorage.User
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor

case class NewUserCreator() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val userCache: Cache[User] = Cache.user

  def getComp = {
    val listOfUsers = SPreformattedText(
      userCache.cell
        .updates().map((c: CacheMap[User]) => c.getPrettyPrintedString)
    ).comp

    val nrOfUsers = SPreformattedText(
      userCache.cell
        .map(c => s"number of users : ${c.getNumberOfEntries.toString}").updates()
    ).comp

    val userName            = STextArea("init_text")
    val createNewUserButton = SButton("Create New User")
    val text                = createNewUserButton.getClick.snapshot(userName.cell)
    val writeToConsole      = SActionWriteToConsole(text)

    // todo-now => create a user ...

    def render: Unit => VdomElement = { _ =>
      <.div(
        listOfUsers(),
        userName.comp(),
        createNewUserButton.vdom(),
        <.br,
        nrOfUsers()
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
