package app.client.ui.components.mainPages.pages.noteHandling.userNoteList
import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cacheInjector.{CacheAndPropsAndRouterCtrl, ToBeWrappedMainPageComponent}
import app.client.ui.components.ListUsersAllNotesPage
import app.client.ui.components.mainPages.helpers.ListRenderHelper
import app.client.ui.components.mainPages.pages.login.UserLoginStatus
import app.client.ui.components.mainPages.pages.noteHandling.noteEditor.NoteEditorComp.NoteEditorPage
import app.client.ui.components.mainPages.pages.noteHandling.userNoteList.ListUsersAllNotesComp.{PropsImpl, StateImpl}
import app.client.ui.components.router.RouterComp
import app.client.ui.components.sodium.SButton
import app.client.ui.dom.Window
import app.shared.comm.postRequests.read.{GetLatestEntityByIDReq, GetUsersNotesReq}
import app.shared.comm.postRequests.read.GetUsersNotesReq.Par
import app.shared.comm.postRequests.write.CreateEntityReq
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.{Image, Note, User}
import app.shared.utils.UUID_Utils.EntityIdentity
import japgolly.scalajs.react.BackendScope
import org.scalajs.dom.html.Anchor

object HelperPrint {

  def prettyPrint(p: Option[EntityWithRef[Note]]): String = {
    val s: String =
      p match {
        case Some(value) => {
          s"""
             |-------------------------------------------
             | Title:
             | ${value.entityValue.title}
             |
             | Content:
             |
             | ${value.entityValue.content}
             |
             | Ref to owner:
             | ${value.entityValue.owner.entityIdentity}
             |
             | Version of Note:
             | ${value.toRef.entityVersion}
             | ${value.toRef.entityValueTypeAsString}
             | ${value.toRef.entityIdentity}
             |
             |""".stripMargin
        }
        case None => "None"
      }
    s
  }

}

trait ListUsersAllNotesComp
    extends ToBeWrappedMainPageComponent[
      ListUsersAllNotesComp,
      ListUsersAllNotesPage
    ] {

  override type StateT = StateImpl
  override type PropsT = PropsImpl

  override def getInitState: StateT = StateImpl()

  override def propsProvider_ : Unit => PropsT = { x =>
    PropsImpl()
  }

  import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
  import japgolly.scalajs.react.vdom.{VdomElement, html_<^}

  val createButton = SButton("Create Node")

  val id: EntityIdentity[User] =
    Window.getUserLoginStatus.userOption.get.toRef.entityIdentity

  val par: CreateEntityReq.CreateEntityReqPar[Note] =
    CreateEntityReq.CreateEntityReqPar(Note("note", "text", id))
  createButton.sClickedSink.listen(
    x => RouterComp.cache.writeToServer[CreateEntityReq[Note]](par)
  )

  override def getVDOM(
    c:            CacheAndPropsAndRouterCtrl[PropsT],
    s:            StateT,
    backendScope: BackendScope[CacheAndPropsAndRouterCtrl[PropsT], StateT]
  ): VdomElement = {

    val userid: UserLoginStatus = Window.getUserLoginStatus

    val r3 = if (!userid.userOption.isDefined) {
      <.div(<.br, "User is not logged in")
    } else {

      object Helper {
        val r2 = userid.userOption.get.toRef.entityIdentity
        val par: Par = Par(r2)

        val res: ReadCacheEntryStates.ReadCacheEntryState[
          GetUsersNotesReq
        ] =
          c.cache.readFromServer[GetUsersNotesReq](par)

        val helper = ListRenderHelper[Note, PropsT](c)

      }

      val g: Option[EntityWithRef[Note]] => VdomElement = {
        val ctl = c.routerCtl

        def linkToEditorPage(
          id: EntityIdentity[Note]
        ): VdomTagOf[Anchor] = {

          val link = <.a(
            "edit",
            ^.href := ctl.urlFor(NoteEditorPage(id.uuid)).value,
            ctl.setOnLinkClick(NoteEditorPage(id.uuid))
          )

          link
        }

        x: Option[EntityWithRef[Note]] => {
          val res =
            x match {
              case Some(value) => {
                val title = value.entityValue.title
                <.div(<.pre(HelperPrint.prettyPrint(x)),
                      title,
                      "  ",
                      linkToEditorPage(value.toRef.entityIdentity),
                  //   c.cache.readFromServer[Req[Image]]()
                  //  todo-now - implement - get all latest entities request
                  //
                      <.br)

              }
              case None => <.div("Loading ...")
            }
          res
        }

      }

      val noteList: html_<^.TagMod =
        if (Helper.res.toOption.isDefined) {
          val r1: GetUsersNotesReq.Res = Helper.res.toOption.get
          val s1 = r1.maybeSet.get
          val l1 = s1.toList

          val r2 =
            l1.map(x => Helper.helper.ref2EntityOption(x)).toList

          val r3: html_<^.TagMod =
            Helper.helper.getEntityListAsVDOM(r2, g)

          r3

        } else {
          <.div("List is Loading ...")
        }

      <.div(<.br, noteList, <.br, createButton.getVDOM())

    }

    r3
  }

}

object ListUsersAllNotesComp extends ListUsersAllNotesComp {

  case class StateImpl()
  case class PropsImpl()

}
