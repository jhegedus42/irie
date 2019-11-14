package client

import client.cache.{Cache, CacheMap, NormalizedStateHolder}
import client.sodium.core.StreamSink
import comm.crudRequests.{GetAllEntityiesForUser, JSONConvertable}
import dataStorage.User
import dataStorage.stateHolder.UserMap
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.Element
import sodium.components.{
  SodiumButton,
  SodiumList,
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

@JSExport("Main")
object Main extends js.JSApp {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val userCache = NormalizedStateHolder.user

  val snapshot = new StreamSink[Unit]()

  val textValue = snapshot
    .snapshot(NormalizedStateHolder.user.cell).map(_.toString)

  @JSExport
  def main(): Unit = {
    val e: Element = document.getElementById("rootComp")
    SodiumPreformattedText(textValue).comp().renderIntoDOM(e)
    TestAjaxRequest.query(snapshot)
  }

//  button.streamSink.listen((x: Unit) => makeTestRequest())

//  val umap = new StreamSink[UserMap]()
//  val cellUmap=umap.hold(UserMap())
//
//  cellUmap.listen(
//        (a:UserMap)=> {
//          val b= a.list.map(_._1)
//          b.foreach(println)
//    }
//  )

}

object TestAjaxRequest {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  import io.circe.syntax._

  val headers: Map[String, String] = Map(
    "Content-Type" -> "application/json"
  )

  val json =
    """
      |{
      |    "par" : {
      |        "uuid" : "53e69a57-5da3-44a7-bb67-e98752ca9a9c"
      |    },
      |    "res" : null
      |}
      |""".stripMargin

  def query(updateRootPage: StreamSink[Unit]): Unit = {

    val ip = "commserver.asuscomm.com"

    val i        = implicitly[JSONConvertable[GetAllEntityiesForUser]]
    val snapshot = new StreamSink[Unit]()
    Ajax
      .post(s"http://$ip:8080/GetAllEntityiesForUser",
            json,
            headers = headers)
      .map(_.responseText).map(i.getObject(_)).onComplete(x => {
        val res1: UserMap = x.toOption.get.res.get
        NormalizedStateHolder.streamToSetInitialCacheState.send(res1)
        snapshot.send(())
      })

  }
}
