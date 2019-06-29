package app.client.ui.routing.componentsThatCanBeRoutedTo.cacheTestRootComp

import org.scalajs.dom.html.Div
import slogging.LazyLogging

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

class Backend($: BackendScope[CacheTestRootCompProps,Unit]) {


  def render( props: CacheTestRootCompProps) = {

    val cache = props.cacheInterface

    val lineSeparator: VdomTagOf[Div] = <.div(<.br, "------------", <.br)

    <.div(
      <.br,
      lineSeparator,
      CacheAccessHelper.getLineTextFromCache(cache),
      lineSeparator,
      CacheAccessHelper.getLineTextFromCache(cache),
      lineSeparator,
      CacheAccessHelper.getLineTextFromCache(cache),
    )
  }

}


object CacheTestRootComp extends LazyLogging {


  lazy val compConstructor =
    ScalaComponent
      .builder[CacheTestRootCompProps]("Cache Experiment")
      .renderBackend[Backend]
      .build

}


