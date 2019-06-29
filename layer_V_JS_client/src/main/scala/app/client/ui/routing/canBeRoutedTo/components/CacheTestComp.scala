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


object CacheTestComp extends LazyLogging {

  lazy val compConstructor: Component[CacheTestRootCompProps, Unit, Backend, CtorType.Props] =
    ScalaComponent
      .builder[CacheTestRootCompProps]("Cache Experiment")
      .renderBackend[Backend]
      .build



}

