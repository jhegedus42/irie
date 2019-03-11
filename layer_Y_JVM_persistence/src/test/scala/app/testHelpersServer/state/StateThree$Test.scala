package app.testHelpersServer.state

import app.shared.data.model.Entity.Data
import org.scalatest.FunSuite
import app.shared.data.utils.PrettyPrint

/**
  * Created by joco on 04/01/2018.
  */
class StateThree$Test extends FunSuite {
  import io.circe.generic.auto._
  import io.circe.parser._
  import io.circe.syntax._
  import io.circe.{Decoder, Encoder}

  test( "testS6val" ) {
    println( "tests6val" )
    val s2 = StateThree.step2
    assert( s2.isRight )
    println( "step 2 is OK :" + s2 )
    val s6 = StateThree.s6val()
    assert( s6.isRight )
    println( "step 6 is right:" + s6 )
    println( "step 6 -------" )
    def f(e: Data ): String = e.toString
    def f2(e: Data ): String = "\n" + PrettyPrint.prettyPrint(e)
    s6.toEither.right.get.stateMap.toList.foreach( x => println( f2( x._2.e ) ) )

  }

}
