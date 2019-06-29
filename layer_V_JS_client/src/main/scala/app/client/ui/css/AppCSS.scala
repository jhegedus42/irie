package app.client.ui.css

import scalacss.Defaults._
import scalacss.internal.mutable.GlobalRegistry
import app.client.ui.routing.generalComponents.TopNavComp
import app.client.ui.routing.canBeRoutedTo.components.HomePage

object AppCSS {

  def load = {
    GlobalRegistry.register(GlobalStyle,
                            TopNavComp.Style,
                            HomePage.Style)
    GlobalRegistry.onRegistration(_.addToDocument())
  }
}
