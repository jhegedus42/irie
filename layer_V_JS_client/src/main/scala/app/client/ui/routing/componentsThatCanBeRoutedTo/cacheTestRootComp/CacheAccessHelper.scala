package app.client.ui.routing.componentsThatCanBeRoutedTo.cacheTestRootComp
import app.client.ui.routing.cache.exposed.CacheInterface
import app.shared.data.ref.{TypedRef, RefVal}
import app.shared.data.model.LineText
import app.testHelpersShared.data.TestEntities
import app.shared.data.ref.{TypedRef, RefVal}
import org.scalajs.dom.html.{Div, Pre}
import slogging.LazyLogging

import scala.util.Random
//import app.shared.data.utils.PrettyPrint
import app.testHelpersShared.data.TestEntities
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.builder.Lifecycle
import japgolly.scalajs.react.vdom.html_<^._

object CacheAccessHelper {

  def getLineTextFromCache(cache:CacheInterface): VdomTagOf[Pre] =
    {
      val ref: TypedRef[LineText] = TypedRef.makeWithUUID[LineText]( TestEntities.refValOfLineV0.r.uuid )
      val rv = cache.readLineText( ref )
      val s= pprint.apply( rv, width = 50,indent = 2 ).plainText
      println(s)
      <.pre(s)
    }

}
