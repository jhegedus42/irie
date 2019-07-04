package app.client.ui.components.mainPageComponents

object MainPageComponentsDeclarations {


  sealed trait MainPageDeclaration

  case object MainPage_HomePage extends MainPageDeclaration
  case object MainPage_CacheTestDemoPage extends MainPageDeclaration
}
