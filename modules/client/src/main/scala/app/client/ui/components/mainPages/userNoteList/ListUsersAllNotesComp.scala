package app.client.ui.components.mainPages.userNoteList
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import app.client.ui.caching.cacheInjector.{CacheAndPropsAndRouterCtrl, ToBeWrappedMainPageComponent}
import app.client.ui.components.{ListUsersAllNotesPage }
import app.client.ui.components.mainPages.userNoteList.ListUsersAllNotesComp.{PropsImpl, StateImpl}
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

  override def propsProvider_ : Unit => PropsT = { x => PropsImpl()
  }

  override def getVDOM(
    c:            CacheAndPropsAndRouterCtrl[PropsT],
    s:            StateT,
    backendScope: BackendScope[CacheAndPropsAndRouterCtrl[PropsT], StateT]
  ): VdomElement = {

    <.div("Hello List of User's Note")

    // todo-now 1.1.2
    //  print list of references
    //  CURRENT-FOCUS

    // todo-now 1.1.2.1
    //  we need to get the list of notes for the current user

    // todo-now 1.1.2.1.1
    //  we need to create a read cache for that request

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
