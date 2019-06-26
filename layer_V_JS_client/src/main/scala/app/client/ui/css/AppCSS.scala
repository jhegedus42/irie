package app.client.ui.css

import scalacss.Defaults._
import scalacss.internal.mutable.GlobalRegistry
import app.client.ui.routing.routersChildren.{LeftNavComp, TopNavComp}
import app.client.ui.components.rootComponents.{HomePage, ItemsPage}

object AppCSS {

  def load = {
    GlobalRegistry.register(GlobalStyle,
                            TopNavComp.Style,
                            LeftNavComp.Style,
                            ItemsPage.Style,
                            HomePage.Style)
    GlobalRegistry.onRegistration(_.addToDocument())
  }
}
