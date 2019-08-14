package app.shared.dataModel.value.refs

import app.shared.dataModel.model.User
import org.scalatest.FunSuite
import io.circe.generic.auto._

class EntityTest extends FunSuite {

  test( "testToJSON" ) {

    val alice = User( "Alice", 38 )
    val refAlice: Entity[User] =
      Entity.makeFromEntity( alice )
    println( s"refAlice as .toString() output : \n $refAlice" )
    println( s"refAlice as JSON : \n ${refAlice.toJSON}" )
    println( "test passed if this json was printed 'nicely'" )


  }

}


