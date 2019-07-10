package app.client.ui.components.router

/**

  */
package object mainPageComp {

  sealed trait MainPageDeclaration

  case object MainPage_HomePage extends MainPageDeclaration
  case object MainPage_CacheTestDemoPage extends MainPageDeclaration

}
