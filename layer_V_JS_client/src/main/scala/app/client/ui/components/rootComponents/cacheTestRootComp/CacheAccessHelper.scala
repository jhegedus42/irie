package app.client.ui.components.rootComponents.cacheTestRootComp
import app.shared.data.ref.{Ref, RefVal}
import app.shared.data.model.LineText
import app.testHelpersShared.data.TestEntities
import app.client.comm.cache.exposed.CacheInterface
import app.shared.data.ref.{Ref, RefVal}
import org.scalajs.dom.html.{Div, Pre}
import slogging.LazyLogging

import scala.util.Random
//import app.shared.data.utils.PrettyPrint
import app.testHelpersShared.data.TestEntities
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.builder.Lifecycle
import japgolly.scalajs.react.vdom.html_<^._

object CacheAccessHelper {

  def getLineTextFromCache: VdomTagOf[Pre] =
    {
      val ref: Ref[LineText] = Ref.makeWithUUID[LineText]( TestEntities.refValOfLineV0.r.uuid )
      val rv = CacheInterface.readLineText( ref )
      val s= pprint.apply( rv, width = 50,indent = 2 ).plainText
      println(s)
      <.pre(s)
    }

}
