package app.client.ui.components

import app.client.ui.caching.cacheInjector.ToBeWrappedMainPageComponent
import app.client.ui.components.mainPages.userHandling.userList.UserListComp


/**
  *
  * The scalajs-react-router needs this trait.
  *
  * Subclasses of this trait correspond to "pages"
  * to which the router can navigate "us".
  *
  * Basically a subclass of this trait corresponds to a
  * "type of URL" / "type of webpage" (as if we were navigating
  * a traditional website - with optional URL parameters).
  *
  * The router will decide what to do with a given URL, for that
  * it uses based the subclasses of this trait - which define the
  * "pages" to which URLs can be "resolved to".
  *
  * A page is an abstract concept. An URL is a string representation
  * of a "page".
  *
  * Subclasses of this trait correspond to these "pages" - these abstract
  * concepts.
  *
  * It is called MainPage because it is the direct and only child of the
  * router. So it is a page, that corresponds to a react component which
  * "sits" at the root of the render VDOM hierarchy, just below the router.
  *
  * So these are the most important react components, hence they are called
  * "Main" components, hence the name for these pages - "Main" pages.
  *
  * A bit of a misnomer because there are no such things as not main pages,
  * but there are components which are not "main" components (that is, they
  * do not correspond/belong to pages) - they are components which can be
  * children of anything - BUT a MainPageComponent can be only a child of
  * the Router. That's it. PUNKTUM.
  *
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

///**
//  *
//  * Add two "thievery" numbers.
//  *
//  * @param number
//  */
//case class ThieveryDemo(number: Int)
//      extends MainPageWithCache[ThieveryDemoComp, ThieveryDemo]

  case class UserListPage()
      extends MainPageWithCache[UserListComp, UserListPage]

  case object StaticTemplatePage extends MainPage

  case class ItemPage(id: Int) extends MainPage


