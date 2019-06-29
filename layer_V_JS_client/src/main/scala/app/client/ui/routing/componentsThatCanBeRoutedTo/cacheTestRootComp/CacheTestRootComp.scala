package app.client.ui.routing.componentsThatCanBeRoutedTo.cacheTestRootComp

import app.client.ui.routing.cache.exposed.CacheInterface
import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import app.testHelpersShared.data.TestEntities
import org.scalajs.dom.html.{Div, Pre}
import slogging.LazyLogging
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

case class CacheTestRootCompProps(s: String, cacheInterface: CacheInterface)

class Backend($: BackendScope[CacheTestRootCompProps, Unit]) {

  def getLineTextFromCache(cache:CacheInterface): VdomTagOf[Pre] =
  {
    val ref: TypedRef[LineText] = TypedRef.makeWithUUID[LineText]( TestEntities.refValOfLineV0.r.uuid )
    val rv = cache.readLineText( ref )
    val s= pprint.apply( rv, width = 50,indent = 2 ).plainText
    println(s)
    <.pre(s)
  }


  def render( props: CacheTestRootCompProps) = {

    val cache = props.cacheInterface

    val lineSeparator: VdomTagOf[Div] = <.div(<.br, "------------", <.br)

    <.div(
      <.br,
      lineSeparator,
      getLineTextFromCache(cache),
      lineSeparator,
      getLineTextFromCache(cache),
      lineSeparator,
      getLineTextFromCache(cache),
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


