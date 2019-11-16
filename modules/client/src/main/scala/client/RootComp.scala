package client
import client.cache.{Cache, CacheMap, NormalizedStateHolder}
import client.sodium.app.reactComponents.SodiumPreformattedText
import dataStorage.User
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor

object RootComp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val userCache: Cache[User] = NormalizedStateHolder.user

  def getComp = {
    val s = userCache.cell
      .updates().map((c: CacheMap[User]) => c.getPrettyPrintedString)

    val sc = SodiumPreformattedText(s).comp

    val rootComp =
      ScalaComponent
        .builder[String]("Hello")
        .render_P(name => <.div("Hello there ", name, sc()))
        .build

    rootComp
  }

  // todo-now, add new user
  // SodiumAction - Insert New Entity

}
