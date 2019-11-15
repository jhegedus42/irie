package client
import client.cache.{Cache, CacheMap, NormalizedStateHolder}
import client.sodium.core.StreamSink
import comm.crudRequests.{GetAllEntityiesForUser, JSONConvertable}
import dataStorage.{RefToEntityOwningUser, User}
import dataStorage.stateHolder.UserMap
import io.circe.Json
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.Element
import sodium.components.{
  SodiumButton,
  SodiumPreformattedText,
  SodiumRootComp
}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Try
import japgolly.scalajs.react.{CtorType, _}
import japgolly.scalajs.react.component.JsFn
import japgolly.scalajs.react.component.ScalaFn.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.internal.Effect.Id
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import org.scalajs.dom.html.Div
import sodium.core._
import testingData.TestEntitiesForUsers

import client.cache.{Cache, CacheMap, NormalizedStateHolder}
import client.sodium.components.SodiumPreformattedText
import dataStorage.User
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.<

import scala.concurrent.ExecutionContextExecutor

object RootComp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val userCache: Cache[User] = NormalizedStateHolder.user

  def getComp = {
    val s = userCache.cell
      .updates().map((c: CacheMap[User]) => c.getPrettyPrintedString)

    val sc = SodiumPreformattedText(s).comp

    val Hello =
      ScalaComponent
        .builder[String]("Hello")
        .render_P(name => <.div("Hello there ", name, sc()))
        .build
    Hello
  }

}
