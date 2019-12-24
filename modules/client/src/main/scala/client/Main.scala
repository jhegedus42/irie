package client

import client.cache.AJAXCalls
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

    AJAXCalls.populateUserEntityCache()
    // todonow 1 Note CRUD
    // todonow 1.1 display set of notes on client (test data)
    // todonow 1.1.1 create cache for set of notes on client
    // todonow 1.1.1.1 populate Note Entity Cache on client
    // todonow 1.1.1.2 refactor Entity populater, abstract over
    //  entities
    // todonow 1.1.2 display list of notes for Alice
    // todonow 1.2 select a note from the list (use the User Selector
    //  Widget as template)
    // todonow 1.3 display the content of the selected note
    // todonow 1.4 create a widget that updates the text of the note
    // todonow 1.5 create a widget that creates a new note
    // todonow 1.6 create a widget that flags a note as "Moved to TrashCan"
    //   todonow 1.7 add trashcan field to a Note
  }
}
