package app.client.ui.css

import scalacss.Defaults._
import scalacss.internal.mutable.GlobalRegistry
import app.client.ui.routing.generalComponents.{LeftNavComp, TopNavComp}
import app.client.ui.routing.componentsThatCanBeRoutedTo.HomePage
import app.client.ui.routing.componentsThatCanBeRoutedTo.itemsComp.ItemsPage

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
