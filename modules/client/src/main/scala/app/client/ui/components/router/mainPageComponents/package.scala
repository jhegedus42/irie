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

  /**
    *
    * Represents a page. This is a data type whose instance (object) corresponds
    * to a "main page" that can be routed to be the "main page" by the router.
    * (Sorry for the lack of better words.)
    *
    * @tparam Comp the component that implements the Page
    *
    * @tparam Page the data type that describes the URL route of the page,
    *              and in general, represents the page for the Router,
    *              it is only needed because of the router, the router
    *              needs this, "representation" of a page, a representation
    *              as an object (which has a MainPageWithCache type)
    */
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
