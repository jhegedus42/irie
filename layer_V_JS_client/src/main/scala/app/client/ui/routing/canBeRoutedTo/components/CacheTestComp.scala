package app.client.ui.routing.canBeRoutedTo.components

import app.client.ui.routing.cache.exposed.CacheInterface
import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import app.testHelpersShared.data.TestEntities
import japgolly.scalajs.react.{CtorType, _}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.{Div, Pre}
import slogging.LazyLogging

case class CacheTestRootCompProps(s: String, cacheInterface: CacheInterface )

class NotWrapped_CacheTestRootComp_Backend(
    $ : BackendScope[CacheTestRootCompProps, Unit]) {

  def render(props: CacheTestRootCompProps ) = {

    val cache = props.cacheInterface

    val lineSeparator: VdomTagOf[Div] = <.div( <.br, "------------", <.br )

    <.div(
      <.br,
      lineSeparator,
      getLineTextFromCache( cache ),
      // WE SHOULD ONLY LAUNCH ONE AJAX REQ AND NOT THREE // TODO
      // WE NEED TO CHECK THAT THIS WORKS, SOME SORT OF "debug counter", nr of ajax calls, etc
      // number of ajax calls "away"
      lineSeparator,
      getLineTextFromCache( cache ),
      lineSeparator,
      getLineTextFromCache( cache )
    )
  }

  def getLineTextFromCache(cache: CacheInterface ): VdomTagOf[Pre] = {
    val ref: TypedRef[LineText] =
      TypedRef.makeWithUUID[LineText]( TestEntities.refValOfLineV0.r.uuid )
    val rv = cache.readLineText( ref )
    val s = pprint.apply( rv, width = 50, indent = 2 ).plainText
    println( s )
    <.pre( s )
  }

}

object CacheTestComp {

  lazy val compConstructor: Component[CacheTestRootCompProps,
                                      Unit,
                                      NotWrapped_CacheTestRootComp_Backend,
                                      CtorType.Props] =
    ScalaComponent
      .builder[CacheTestRootCompProps]( "Cache Experiment" )
      .renderBackend[NotWrapped_CacheTestRootComp_Backend]
      .build

}
