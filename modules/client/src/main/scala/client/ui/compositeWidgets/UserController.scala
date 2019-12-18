package client.ui.compositeWidgets

import client.cache.{Cache, CacheMap}
import client.ui.atomicWidgets.input.{SButton, STextArea}
import client.ui.atomicWidgets.templates.CellTemplate
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{TypedReferencedValue, User}
import scala.concurrent.ExecutionContextExecutor

case class UserController() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val userCache: Cache[User] = Cache.user

  lazy val selectedUser = new CellSink[Option[User]](None)

  val updateUserNameComp = {

    // todonow 1 create UPDATE command in cache

    <.div(
      )

  }

  lazy val listOfUsersComp = SWPreformattedText(
    userCache.cellLoop
      .updates().map(
        (c: CacheMap[User]) => c.getPrettyPrintedString
      )
  ).comp

  lazy val listOfUsersWithCellTemplateComp = {
    val c: CellLoop[CacheMap[User]] = userCache.cellLoop

    def user2VDOMList(u: User): List[VdomElement] = {
      val name: VdomTagOf[Div] = <.div(u.name)
      val favNumber: VdomTagOf[Div] =
        <.div(u.favoriteNumber.toString)
      val pwd = <.div(u.password)
      val selectButton = SButton("select", { () =>
        selectedUser.send(Some(u))
      })

      //todo now ^ add user selector button

      List(name, favNumber, pwd, selectButton.vdom())
    }

    val t = CellTemplate(
      c, { x: CacheMap[User] =>
        <.div(
          TableHelpers.getTableFromVdomElements(
            x.map.values.toList
              .map(_.entityValue).map(user2VDOMList)
          )
        )
      }
    )
    t.getComp
  }

  lazy val nrOfUsersComp = SWPreformattedText(
    userCache.cellLoop
      .map(
        c => s"number of users : ${c.getNumberOfEntries.toString}"
      ).updates()
  ).comp

  lazy val userNameInputComp: STextArea = STextArea("init_text")

  lazy val createNewUserButtonComp = SButton(
    "Create New User",
    () => {
      println("i was pusssshed")

      val text = userNameInputComp.cell.sample()

      val newUser =
        TypedReferencedValue[User](
          User(name = text, favoriteNumber = 46)
        )

      userCache.inserterStream.send(newUser)

    }
  )

  lazy val displaySelectedUserComp =
    new CellTemplate[Option[User]](selectedUser, { x =>
      <.pre(s"selected user: $x")
    })

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.h2("User Controller"),
        <.hr,
        <.br,
        listOfUsersComp(),
        userNameInputComp.comp(),
        createNewUserButtonComp.vdom(),
        <.br,
        nrOfUsersComp(),
        listOfUsersWithCellTemplateComp(),
        displaySelectedUserComp.getComp(),
        <.hr,
        <.br
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
