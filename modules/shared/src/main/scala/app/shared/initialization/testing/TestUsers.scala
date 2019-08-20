package app.shared.initialization.testing

import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.utils.UUID_Utils

object TestUsers {

  val alice      = User( "Alice", 38 )
  val bob        = User( "Bob", 45 )
  val kutya      = User( "Kutya", 42 )
  val cica       = User( "Cica", 137 )
  val meresiHiba = User( "MeresiHiba", 369 )

  val aliceEntity: Entity[User] = Entity.makeFromValue( alice )
  val bobEntity:   Entity[User] = Entity.makeFromValue( bob )
  import monocle.macros.syntax.lens._

  val aliceEntity_with_UUID0 = aliceEntity
    .lens( _.refToEntity.entityIdentity.uuid ).set( UUID_Utils.uuid00 )

  val bobEntity_with_UUID1 = bobEntity
    .lens( _.refToEntity.entityIdentity.uuid ).set( UUID_Utils.uuid01 )
}
