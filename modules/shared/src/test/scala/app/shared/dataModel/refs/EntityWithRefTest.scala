package app.shared.dataModel.refs

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import org.scalatest.FunSuite
import io.circe.generic.auto._

class EntityWithRefTest extends FunSuite {

  test( "testToJSON" ) {

    val alice = User( "Alice", 38 )
    val entityAlice: EntityWithRef[User] =
      EntityWithRef.makeFromValue( alice )

    println( s"\n\nentity for Alice as nice String : \n${entityAlice.entityAsJSON()}\n\n" )

    println( "\n\ntest passed 'Alice as JSON' was printed 'nicely'\n\n" )


  }

}


