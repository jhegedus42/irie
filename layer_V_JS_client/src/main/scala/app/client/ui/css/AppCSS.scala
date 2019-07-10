package app.client.ui.css

import scalacss.Defaults._
import scalacss.internal.mutable.GlobalRegistry
import app.client.ui.components.generalComponents.TopNavComp
import app.client.ui.components.router.mainPageComp.HomePageComp

object AppCSS {

  def load = {
    GlobalRegistry.register( GlobalStyle, TopNavComp.Style, HomePageComp.Style )
    GlobalRegistry.onRegistration( _.addToDocument() )
  }
}
