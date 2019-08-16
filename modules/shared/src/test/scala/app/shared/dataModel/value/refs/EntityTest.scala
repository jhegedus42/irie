package app.shared.dataModel.value.refs

import app.shared.dataModel.model.User
import org.scalatest.FunSuite
import io.circe.generic.auto._

class EntityTest extends FunSuite {

  test( "testToJSON" ) {

    val alice = User( "Alice", 38 )
    val entityAlice: Entity[User] =
      Entity.makeFromValue( alice )

    println( s"\n\nentity for Alice as nice String : \n${entityAlice.entityAsString()}\n\n" )

    println( "\n\ntest passed 'Alice as JSON' was printed 'nicely'\n\n" )


  }

}


