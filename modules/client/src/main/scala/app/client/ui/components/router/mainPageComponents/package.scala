package app.client.ui.components.router
/**

  The list of components that can appear as the
  "main pages" of the app. In other words, as a
  direct and at a given time one and only child of
  the router `app.client.ui.components.router.RouterComp` .

*/

package object mainPageComponents {

  /**
    * This denotes that a subclass can be a single, direct child of Router,
    * representing the only single page that is at a given time is displayed
    * in the browser.
    */
  sealed trait MainPage

  case object HomePage extends MainPage

  case object CacheTestDemoPage extends MainPage

  case object NoteListPage extends MainPage


}
