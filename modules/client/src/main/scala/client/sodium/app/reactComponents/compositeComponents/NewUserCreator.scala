package client.sodium.app.reactComponents.compositeComponents

import client.cache.{Cache, CacheMap}
import client.sodium.app.actions.SActionWriteToConsole
import client.sodium.app.reactComponents.atomicComponents.{
  CellTemplate,
  SButton,
  SPreformattedText,
  STextArea
}
import client.sodium.core
import client.sodium.core._
import dataStorage.{ReferencedValue, User}
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
      userCache.cellLoop
        .updates().map((c: CacheMap[User]) => c.getPrettyPrintedString)
    ).comp

    val nrOfUsers = SPreformattedText(
      userCache.cellLoop
        .map(c => s"number of users : ${c.getNumberOfEntries.toString}").updates()
    ).comp

    val userName: STextArea = STextArea("init_text")

    val createNewUserButton =
      SButton(
        "Create New User",
        () => {
          println("i was pusssshed")

          val text = userName.cell.sample()

          val newUser =
            ReferencedValue[User](User(name = text, favoriteNumber = 46))

          userCache.inserterStream.send(newUser)

        }
      )

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
