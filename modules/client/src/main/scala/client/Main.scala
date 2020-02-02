package client

import client.cache.{AJAXCalls, Cache}
import client.ui.RootComp
import client.ui.helpers.Router
import org.scalajs.{dom => d}
import org.scalajs.dom.raw.Element
import org.scalajs.dom.ext.Ajax
import shared.testingData.TestEntitiesForUsers

import scala.collection.immutable.HashMap
import io.circe.syntax._
import shared.dataStorage.model.PWDNotHashed
import shared.dataStorage.stateHolder.UserMap
//import io.circe.generic.JsonCodec
//import io.circe.generic.auto._
import io.circe.{Decoder, Json}
import shapeless.Typeable
//import io.circe.parser._
import shapeless.Typeable

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import io.circe.syntax._

@JSExport("Main")
object Main extends js.JSApp {

  import js.Dynamic.{global => g, newInstance => jsnew}
  val port: Int    = g.configObjFromServer.port.asInstanceOf[Int]
  val host: String = g.configObjFromServer.host.asInstanceOf[String]

  def getPWDNotHashed: PWDNotHashed = {
//    ???

    PWDNotHashed("titok")
  }

  @JSExport
  def main(): Unit = {

    Router.disableBackButton()

    val e: Element = d.document
      .getElementById("rootComp")

    RootComp.getComp().renderIntoDOM(e)

    AJAXCalls.populateEntityCache(Cache.userCache,getPWDNotHashed)

    AJAXCalls.populateEntityCache(Cache.noteCache,getPWDNotHashed)

    AJAXCalls.populateEntityCache(Cache.folderCache,getPWDNotHashed)

  }

}
