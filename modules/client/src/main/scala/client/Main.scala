package client

import client.cache.AJAXCalls
import client.ui.RootComp
import client.ui.router.Router
import org.scalajs.{dom => d}
import org.scalajs.dom.raw.Element
import dataStorage.stateHolder.UserMap
import org.scalajs.dom.ext.Ajax
import testingData.TestEntitiesForUsers

import scala.collection.immutable.HashMap
import io.circe.Decoder.Result
import io.circe._
import io.circe.Json
import io.circe.syntax._
//import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.{Decoder, Json}
import shapeless.Typeable
import io.circe.parser._
import shapeless.Typeable

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import io.circe.syntax._

@JSExport("Main")
object Main extends js.JSApp {

  @JSExport
  def main(): Unit = {

    Router.disableBackButton()

    val e: Element = d.document
      .getElementById("rootComp")

    RootComp.getComp().renderIntoDOM(e)

    AJAXCalls.populateUserEntityCache()
  }
}
