package app.client.ui.components.mainPages.userNoteList
import app.client.ui.caching.cache.ReadCacheEntryStates
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import app.client.ui.caching.cacheInjector.{CacheAndPropsAndRouterCtrl, ToBeWrappedMainPageComponent}
import app.client.ui.components.ListUsersAllNotesPage
import app.client.ui.components.mainPages.userHandling.userList.ListRenderHelper
import app.client.ui.components.mainPages.userNoteList.ListUsersAllNotesComp.{PropsImpl, StateImpl}
import app.shared.comm.ReadRequest
import app.shared.comm.postRequests.GetUsersNotesReq
import app.shared.comm.postRequests.GetUsersNotesReq.Par
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.Note
import app.shared.initialization.testing.TestEntitiesForUsers
import japgolly.scalajs.react.BackendScope

import scala.collection.immutable

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

  override def getVDOM(
    c:            CacheAndPropsAndRouterCtrl[PropsT],
    s:            StateT,
    backendScope: BackendScope[CacheAndPropsAndRouterCtrl[PropsT], StateT]
  ): VdomElement = {


    val a = TestEntitiesForUsers.aliceEntity_with_UUID0
    val par: Par = Par(a.toRef.entityIdentity)

    val res
      : ReadCacheEntryStates.ReadCacheEntryState[ReadRequest,
                                                 GetUsersNotesReq] =
      c.cache.readFromServer[ReadRequest, GetUsersNotesReq](par)

    val helper = ListRenderHelper[Note,PropsT](c)

    import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
    import japgolly.scalajs.react.vdom.{VdomElement, html_<^}

    val noteList: html_<^.TagMod = if (res.toOption.isDefined) {
      val r1: GetUsersNotesReq.Res =res.toOption.get
      val s1=r1.maybeSet.get
      val l1=s1.toList

      val r2 = l1.map(x=>helper.ref2EntityOption(x)).toList

      val g : Option[EntityWithRef[Note]] => VdomElement = {

        x=>
          {
            val res: String =
              x match {
                case Some(value) => value.entityValue.title
                case None => "Loading ..."
              }
            <.div(res,<.br)
          }

      }

      val r3: html_<^.TagMod = helper.getEntityListAsVDOM(r2,g)
      r3

    } else {
      <.div("List is Loading ...")
    }

    <.div("Hello List of User's Note 42", <.br, noteList)

  }

}

object ListUsersAllNotesComp extends ListUsersAllNotesComp {

  case class StateImpl()
  case class PropsImpl()

  // todo-now 1.1
  //  create a Note List page, similar to User List Page
  //  with CRU(D) capabilities

  // todo-now 1.1.4
  //  print list of note titles, and link to the their "editor page"

  // todo-now 1.1.4 - 1
  //  create a page that can edit a Note (to which we can link)
  //  use the user editor page as "inspiration for this
  //  CURRENT FOCUS
}
