package app.server.httpServer

import app.server.utils.PrettyPrint
import app.shared.dataModel.model.User
import app.shared.utils.macros.compilationTime.AppendCompilationTimeToString
import org.scalatest.FunSuite

case class Test(string:String)

class PersistenceModuleTest extends FunSuite {

  test("testInit") {
    println(s"This is where the Persistence Module will be tested.")

  }

  @AppendCompilationTimeToString
  val compilationTime : String = ""

  test("compilation time macro test"){

    println(s"compilation time was : $compilationTime")

  }

  test("pretty print Alice"){
    val alice = User( "Alice", 38 )
    val pretty_alice= PrettyPrint.prettyPrint(alice)
    print(s"\n\nalice pretty printed at compilation time of '$compilationTime' is :\n$pretty_alice\n\n")
  }

}
