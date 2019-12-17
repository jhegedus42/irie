package client.ui.compositeWidgets

import client.cache.{Cache, CacheMap}
import client.ui.atomicWidgets.input.{SButton, STextArea}
import client.ui.atomicWidgets.templates.CellTemplate
import client.sodium.core.CellLoop
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

  def getComp = {

    val listOfUsers = SWPreformattedText(
      userCache.cellLoop
        .updates().map(
          (c: CacheMap[User]) => c.getPrettyPrintedString
        )
    ).comp

    def user2VDOMList(u: User): List[VdomElement] = {
      val name:      VdomTagOf[Div] = <.div(u.name)
      val favNumber: VdomTagOf[Div] = <.div(u.favoriteNumber.toString)
      val pwd = <.div(u.password)
      List(name, favNumber, pwd)
    }

    val listOfUsersWithCellTemplate = {
      val c: CellLoop[CacheMap[User]] = userCache.cellLoop
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

    val nrOfUsers = SWPreformattedText(
      userCache.cellLoop
        .map(
          c => s"number of users : ${c.getNumberOfEntries.toString}"
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
//        listOfUsers(),
        userNameInput.comp(),
        createNewUserButton.vdom(),
        <.br,
        nrOfUsers(),
        listOfUsersWithCellTemplate()
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
