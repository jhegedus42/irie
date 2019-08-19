package app.shared.dataModel.testUsers

import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User

object TestUsers {

  val alice      = User( "Alice", 38 )
  val bob        = User( "Bob", 45 )
  val kutya      = User( "Kutya", 42 )
  val cica       = User( "Cica", 137 )
  val meresiHiba = User( "MeresiHiba", 369 )

  val aliceEntity: Entity[User] = Entity.makeFromValue(alice)

}

