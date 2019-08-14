package app.shared.dataModel.entity.refs

import app.shared.dataModel.model.User
import org.scalatest.FunSuite
import io.circe.generic.auto._

class TypedRefValTest extends FunSuite {

  test( "testToJSON" ) {

    val alice = User( "Alice", 38 )
    val refAlice: TypedRefVal[User] =
      TypedRefVal.makeFromEntity( alice )
    println( s"refAlice as .toString() output : \n $refAlice" )
    println( s"refAlice as JSON : \n ${refAlice.toJSON}" )
    println( "test passed if this json was printed 'nicely'" )


  }

}
