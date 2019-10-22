package app.client.ui.components.mainPages.userNoteList
import app.client.ui.caching.cache.ReadCacheEntryStates
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import app.client.ui.caching.cacheInjector.{
  CacheAndPropsAndRouterCtrl,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.ListUsersAllNotesPage
import app.client.ui.components.mainPages.userNoteList.ListUsersAllNotesComp.{
  PropsImpl,
  StateImpl
}
import app.shared.comm.ReadRequest
import app.shared.comm.postRequests.GetUsersNotesReq
import app.shared.comm.postRequests.GetUsersNotesReq.Par
import app.shared.initialization.testing.TestEntitiesForUsers
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.vdom.VdomElement

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

    // todo-now 1.1.2
    //  print list of references to Alice's notes

    // todo-now 1.1.2 - 1
    //  we need to get the list of notes for Alice

    // todo-now 1.1.2 - 1.1
    //  we need to look at user list comp, how the request
    //  is made for that
    //  read this :
    //  app.client.ui.components.mainPages.userHandling.userList.UserListRenderLogic.userListAsVDOM

    // todo-now 1.1.2 - 1.1.1
    //  we need to create a parameter for the request
    //  for that we need to look at the test cases
    //  look at this :
    //  org.scalatest.FunSuiteLike.test
    //  CURRENT FOCUS

    val a = TestEntitiesForUsers.aliceEntity_with_UUID0
    val par: Par = Par(a.toRef.entityIdentity)

    val res
      : ReadCacheEntryStates.ReadCacheEntryState[ReadRequest,
                                                 GetUsersNotesReq] =
      c.cache.readFromServer[ReadRequest, GetUsersNotesReq](par)

    val s = if (res.toOption.isDefined) {
      val s1 = res.toOption.get.maybeSet.get.toString()
      s1
    } else {
      "List is Loading ..."
    }

    <.div("Hello List of User's Note", <.br, s)

  }

}

object ListUsersAllNotesComp extends ListUsersAllNotesComp {

  case class StateImpl()
  case class PropsImpl()

  // todo-now 1.1
  //  create a Note List page, similar to User List Page
  //  with CRU(D) capabilities
  // todo-now 1.1.3
  //  print list of note titles

  // todo-now 1.1.4
  //  print list of note titles, and link to the their "editor page"
}
