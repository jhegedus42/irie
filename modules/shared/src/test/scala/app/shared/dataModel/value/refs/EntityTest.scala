package app.shared.dataModel.value.refs

import app.shared.dataModel.model.User
import org.scalatest.FunSuite
import io.circe.generic.auto._

class EntityTest extends FunSuite {

  test( "testToJSON" ) {

    val alice = User( "Alice", 38 )
    val entityAlice: Entity[User] =
      Entity.makeFromValue( alice )
    println( s"entity for Alice as .toString() output : \n $entityAlice" )

    println( s"entity for Alice as JSON : \n ${entityAlice.toJSON}" )

    println( "test passed if this json was printed 'nicely'" )


  }

}


