package app
  .client.ui.components.mainPageComponents.components.cacheTestMainPageComp
import app.client.ui.caching.CacheInterface
import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import app.testHelpersShared.data.TestEntities
import japgolly.scalajs.react.CtorType
import japgolly.scalajs.react.component.Scala.Component
import org.scalajs.dom.html.{Div, Pre}

case class CacheTestRootCompProps(s: String, cacheInterface: CacheInterface )
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

class NotWrapped_CacheTestRootComp_Backend(
    $ : BackendScope[CacheTestRootCompProps, Unit]) {

  def render(props: CacheTestRootCompProps ) = {

    val cache = props.cacheInterface

    val lineSeparator: VdomTagOf[Div] = <.div( <.br, "------------", <.br )

    <.div(
      <.br,
      lineSeparator,
      getLineTextFromCache( cache ),
      lineSeparator,
      getLineTextFromCache( cache ),
      lineSeparator,
      getLineTextFromCache( cache ),
      AddTheThieveryNumbersUsingTheClientOnly.TheCorporation(),
      AddTheThieveryNumbersUsingTheServer.TheCorporation()
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
