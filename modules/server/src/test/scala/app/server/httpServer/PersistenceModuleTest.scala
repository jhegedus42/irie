package app.server.httpServer

import app.server.httpServer.persistence.PersistenceModule
import app.server.utils.PrettyPrint
import app.shared.dataModel.model.User
import app.shared.utils.macros.compilationTime.AppendCompilationTimeToString
import org.scalatest.FunSuite

case class Test(string:String)

class PersistenceModuleTest extends FunSuite {

  test("testInit") {
    println(s"\n\nThis is where the Persistence Module will be tested.\n\n")

  }

  @AppendCompilationTimeToString
  val compilationTime : String = ""

  test("compilation time macro test"){

    println(s"\n\nCompilation time for PersistenceModuleTest was : $compilationTime\n\n")

  }

  test("pretty print Alice"){
    val alice = User( "Alice", 38 )
    val pretty_alice= PrettyPrint.prettyPrint(alice)
    print(s"\n\nalice pretty printed at compilation time of '$compilationTime' is :\n$pretty_alice\n\n")
  }

  test("init"){

    def init(): Unit ={

      val alice= User("Alice",38)
      val bob= User("Bob",45)
      val kutya= User("Kutya",42)
      val cica= User("Cica",137)
      val meresiHiba= User("MeresiHiba",369)

      val pm = PersistenceModule()

      //  step 1 - csinaljunk beloluk valamit amit bele lehet
      //   tenni HOVA ?
      //
      //


      //     - de mit eszik a Journal ? => ki kell talalni
      //       - hol van a Journal ?
      //       - figure out how to store the entities in the application state
      //       - sima Entity -kent casting-gal
      //  step 2 - tegyuk bele a journal-ba / application state-be
      //  step 3 - olvassuk ki az application state tartalmat es irjuk
      //           ki a konzolra

    }

  }

}
