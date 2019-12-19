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

  lazy val userNameUpdater = {

    // todonow 1 - update user

    // todonow 1.1 update user name component
    //  - 1.1.1 show pre-textfield filled with currently selected user's name
    //    - CONTINUE-NOW
    //    - we need to copy the selected user component
    //    - we need to search for a textfield
    //  - 1.1.2 button, which, when clicked will send an update-request to the server
    //

    lazy val nameOfSelectedUser = STextArea(
      "",
      selectedUser
        .updates().map(
          x => x.map(_.name).getOrElse("no user selected yet")
        )
    )

    lazy val updateNameButton = SButton(
      "update name",
      () => {
        selectedUser.listen({ (x: Option[User]) =>
          val newName = nameOfSelectedUser.cell.sample()
          println(
            s"we should update $x 's name to $newName"
          )

        // todonow - CONTINUE HERE
        //  send AJAX req to update user name on server
        //  via the Cache

        })

      }
    )

    lazy val comp =
      new CellTemplate[Option[User]](selectedUser, { x =>
        <.div(
          <.pre(s"selected user: $x"),
          nameOfSelectedUser.comp(),
          updateNameButton.comp()
        )
      })

    comp

  }

  lazy val listOfUsers = SWPreformattedText(
    userCache.cellLoop
      .updates().map(
        (c: CacheMap[User]) => c.getPrettyPrintedString
      )
  )

  lazy val tableOfUsersWithSelectorButton = {
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

      List(name, favNumber, pwd, selectButton.comp())
    }

    val t = CellTemplate(
      c, { x: CacheMap[User] =>
        <.div(
          TableHelpers.getTableFromVdomElements(
            x.map.values.toList
              .map(_.versionedEntityValue.valueWithoutVersion).map(
                user2VDOMList
              )
          )
        )
      }
    )

    t
  }

  lazy val nrOfUsers = SWPreformattedText(
    userCache.cellLoop
      .map(
        c => s"number of users : ${c.getNumberOfEntries.toString}"
      ).updates()
  )

  lazy val newUsersNameTextField: STextArea = STextArea("init_text")

  lazy val createNewUserButton = SButton(
    "Create New User",
    () => {
      println("i was pusssshed")

      val text = newUsersNameTextField.cell.sample()

      val newUser =
        TypedReferencedValue[User](
          User(name = text, favoriteNumber = 46)
        )

      userCache.insertEntityStream.send(newUser)

    }
  )

  lazy val selectedUserAsText =
    new CellTemplate[Option[User]](selectedUser, { x =>
      <.pre(s"selected user: $x")
    })

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.h2("User Controller (new)"),
        <.hr,
        <.br,
        listOfUsers.comp(),
        newUsersNameTextField.comp(),
        createNewUserButton.comp(),
        <.br,
        nrOfUsers.comp(),
        tableOfUsersWithSelectorButton.comp(),
        selectedUserAsText.comp(),
        userNameUpdater.comp(),
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
