package app.client.ui.components.router.mainPageComp.cacheTestMPC
import app.client.ui.caching.CacheInterface
import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import app.testHelpersShared.data.TestEntities
import japgolly.scalajs.react.CtorType
import japgolly.scalajs.react.component.Scala.Component
import org.scalajs.dom.html.{Div, Pre}

case class CacheTest_RootComp_Props(s: String, cacheInterface: CacheInterface )
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

class NotWrapped_CacheTestRootComp_Backend(
    $ : BackendScope[CacheTest_RootComp_Props, Unit]) {

  def render(props: CacheTest_RootComp_Props ) = {

    val cache = props.cacheInterface

    val lineSeparator: VdomTagOf[Div] = <.div( <.br, "------------", <.br )

    <.div(
      <.br,
      AddTheThieveryNumbersUsingTheServer.TheCorporation( cache ),
      lineSeparator,
      getLineTextFromCache( cache ),
      lineSeparator,
      getLineTextFromCache( cache ),
      lineSeparator,
      getLineTextFromCache( cache ),
      AddTheThieveryNumbersUsingTheClientOnly.TheCorporation()
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

  lazy val compConstructor: Component[CacheTest_RootComp_Props,
                                      Unit,
                                      NotWrapped_CacheTestRootComp_Backend,
                                      CtorType.Props] =
    ScalaComponent
      .builder[CacheTest_RootComp_Props]( "Cache Experiment" )
      .renderBackend[NotWrapped_CacheTestRootComp_Backend]
      .build

}
