package app
  .client.ui.components.mainPageComponents.components.cacheTestMainPageComp
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
      AddTheThieveryNumbersUsingTheServer.TheCorporation(cache),
        // TODO remember the state - using the
        // /Users/joco/dev/im/irie/layer_V_JS_client/src/main/scala/app/client/ui/caching/localState/TypedRefToClientState.scala
        // so when I navigate away from the CacheTest page and then back
        // the thievery numbers that I entered are remembered
        // i.e. the state is not stored in the react component's local state
        // but in the "global state"
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
