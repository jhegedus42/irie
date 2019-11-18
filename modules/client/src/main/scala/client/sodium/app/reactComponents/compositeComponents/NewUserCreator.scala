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

//    def inserter : StreamSink

    val createNewUserButton =
      SButton("Create New User", () => println("i was pusssshed"))

//    val text: core.Stream[String] =
//      createNewUserButton.getClick.snapshot(userName.cell)
//    `
//    val newUser: core.Stream[ReferencedValue[User]] =
//      text.map(n => ReferencedValue(User(name = n, favoriteNumber = 46)))
//    val writeToConsole = SActionWriteToConsole(newUser.map(x => x.toString()))

//    userCache.inserterStream.
//    newUser.listen(
//      (v: ReferencedValue[User]) => userCache.inserterStream.send(v)
//    )

    // todo-now => create a user ... FIX THIS ^^^^

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
