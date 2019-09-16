package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.ToBeWrappedMainPageComponent
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp
import app.client.ui.components.router.mainPageComponents.userEditor.AllUserListPageComp

/**
  **
 The list of components that can appear as the
  *"main pages" of the app. In other words, as a
  *direct and at a given time one and only child of
  *the router `app.client.ui.components.router.RouterComp` .
  *
  */
package object mainPageComponents {

  /**
    * This denotes that a subclass can be a single, direct child of Router,
    * representing the only single page that is at a given time is displayed
    * in the browser.
    */
  trait MainPage

  trait MainPageWithCache[
    Comp <: ToBeWrappedMainPageComponent[Comp, Page],
    Page <: MainPageWithCache[Comp, Page]]
      extends MainPage

  case object LoginPage extends MainPage

  case class SumIntPage(number: Int)
      extends MainPageWithCache[SumIntComp, SumIntPage]

  /**
    *
    */
  case class AllUserListPageWithCache(string: String)
      extends MainPageWithCache[AllUserListPageComp, AllUserListPageWithCache]

  //  case class UserEditorPage(uuid: String) extends MainPage

  case object NoteListPage extends MainPage

  case object AdminPage extends MainPage

  case class ItemPage(id: Int) extends MainPage

//  case class StaticEditorPage(uuid: String) extends MainPage

}
