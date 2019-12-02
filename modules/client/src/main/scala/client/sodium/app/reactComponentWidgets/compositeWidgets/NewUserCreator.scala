package client.sodium.app.reactComponentWidgets.compositeWidgets

import client.cache.{Cache, CacheMap}
import client.sodium.app.reactComponentWidgets.atomicWidgets.displayOnlyWidgets.SWPreformattedText
import client.sodium.app.reactComponentWidgets.atomicWidgets.inputWidgets.{
  SButton,
  STextArea
}
import dataStorage.{TypedReferencedValue, User}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

import scala.concurrent.ExecutionContextExecutor

case class NewUserCreator() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val userCache: Cache[User] = Cache.user

  def getComp = {

    val listOfUsers = SWPreformattedText(
      userCache.cellLoop
        .updates().map(
          (c: CacheMap[User]) => c.getPrettyPrintedString
        )
    ).comp

    val nrOfUsers = SWPreformattedText(
      userCache.cellLoop
        .map(
          c =>
            s"number of users : ${c.getNumberOfEntries.toString}"
        ).updates()
    ).comp

    val userNameInput: STextArea = STextArea("init_text")

    val createNewUserButton =
      SButton(
        "Create New User",
        () => {
          println("i was pusssshed")

          val text = userNameInput.cell.sample()

          val newUser =
            TypedReferencedValue[User](
              User(name = text, favoriteNumber = 46)
            )

          userCache.inserterStream.send(newUser)

        }
      )

    def render: Unit => VdomElement = { _ =>
      <.div(
        listOfUsers(),
        userNameInput.comp(),
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
