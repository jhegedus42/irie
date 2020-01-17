package client

import client.cache.{AJAXCalls, Cache}
import client.ui.RootComp
import client.ui.helpers.Router
import org.scalajs.{dom => d}
import org.scalajs.dom.raw.Element
import org.scalajs.dom.ext.Ajax
import shared.testingData.TestEntitiesForUsers

import scala.collection.immutable.HashMap
import io.circe.Decoder.Result
import io.circe._
import io.circe.Json
import io.circe.syntax._
import shared.dataStorage.stateHolder.UserMap
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

    AJAXCalls.populateEntityCache(Cache.userCache)

    AJAXCalls.populateEntityCache(Cache.noteCache)

    AJAXCalls.populateEntityCache(Cache.imgCache)

    AJAXCalls.populateEntityCache(Cache.folderCache)
  }
}
