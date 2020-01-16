package client.ui.compositeWidgets.archive

import client.cache.commands.UpdateEntityInCacheCmd
import client.cache.{Cache, CacheMap}
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.input.{SButton, STextArea}
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{TypedReferencedValue, User}

import scala.concurrent.ExecutionContextExecutor

case class UserAdminWidget() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val userCache: Cache[User] = Cache.userCache

  lazy val selectedUserCell =
    new CellSink[Option[TypedReferencedValue[User]]](None)

  lazy val userNameUpdaterTextAreaAndButton = {

    lazy val nameOfSelectedUser = STextArea(
      "",
      selectedUserCell
        .updates().map(
          x =>
            x.map(_.versionedEntityValue.valueWithoutVersion.name).getOrElse(
                "please select user"
              )
        )
    )

    lazy val updateNameButton = SButton(
      "update name",
      Some(() => {

        val user = {
          selectedUserCell.sample()
        }

        user match {
          case Some(trv) => {
            val newName = nameOfSelectedUser.cell.sample()
            println(
              s"we should update $user 's name to $newName"
            )
            import monocle.macros.syntax.lens._
            val newUser =
              trv.versionedEntityValue.valueWithoutVersion
                .lens(_.name).set(newName)

            userCache.updateEntityCommandStream.send(
              UpdateEntityInCacheCmd(trv, newUser)
            )

            selectedUserCell.send(None)

          }
          case None => {
            println(
              "No user has been selected, we cannot update anything."
            )
          }
        }

      })
    )

    lazy val comp =
      new CellTemplate[Option[TypedReferencedValue[User]]](
        selectedUserCell, { x =>
          <.div(
            <.pre(s"selected user: $x"),
            nameOfSelectedUser.comp(),
            updateNameButton.comp()
          )
        }
      )

    comp

  }

  lazy val createNewUserButton = SButton(
    "Create New User",
    Some(() => {
      println("i was pusssshed")

      val text = newUsersNameTextField.cell.sample()

      val newUser =
        TypedReferencedValue[User](
          User(name = text, favoriteNumber = 46)
        )

      userCache.insertEntityStream.send(newUser)
    })
  )

  lazy val newUsersNameTextField: STextArea = STextArea("init_text")

  lazy val listOfUsers = SWPreformattedText(
    userCache.cellLoop
      .updates().map(
        (c: CacheMap[User]) => c.getPrettyPrintedString
      )
  )

  lazy val nrOfUsersLabel = SWPreformattedText(
    userCache.cellLoop
      .map(
        c => s"number of users : ${c.getNumberOfEntries.toString}"
      ).updates()
  )

  lazy val tableOfUsersWithSelectorButton = {
    val c: CellLoop[CacheMap[User]] = userCache.cellLoop

    def user2VDOMList(
      u: TypedReferencedValue[User]
    ): List[VdomElement] = {
      val name: VdomTagOf[Div] =
        <.div(u.versionedEntityValue.valueWithoutVersion.name)

      val favNumber: VdomTagOf[Div] =
        <.div(
          u.versionedEntityValue.valueWithoutVersion.favoriteNumber.toString
        )

      val pwd =
        <.div(u.versionedEntityValue.valueWithoutVersion.password)

      val selectButton = SButton("select", {
        Some(() => selectedUserCell.send(Some(u)))
      })

      //todo now ^ add user selector button

      List(name, favNumber, pwd, selectButton.comp())

    }

    val t = CellTemplate(
      c, { x: CacheMap[User] =>
        <.div(
          TableHelpers.getTableFromVdomElements(
            x.cacheMap.values.toList
              .map(
                user2VDOMList
              )
          )
        )
      }
    )

    t
  }

  lazy val selectedUserAsText =
    new CellTemplate[Option[TypedReferencedValue[User]]](
      selectedUserCell, { x =>
        <.pre(s"selected user: $x")
      }
    )

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
        nrOfUsersLabel.comp(),
        tableOfUsersWithSelectorButton.comp(),
        selectedUserAsText.comp(),
        userNameUpdaterTextAreaAndButton.comp(),
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
