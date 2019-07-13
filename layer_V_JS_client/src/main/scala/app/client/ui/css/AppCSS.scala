package app.client.ui.css

import scalacss.Defaults._
import scalacss.internal.mutable.GlobalRegistry
import app.client.ui.components.generalComponents.TopNavComp
import app.client.ui.components.router.mainPageComp.HomeMPC

object AppCSS {

  def load(): Unit = {
    GlobalRegistry.register( GlobalStyle, TopNavComp.Style, HomeMPC.Style )
    GlobalRegistry.onRegistration( _.addToDocument() )
  }
}
