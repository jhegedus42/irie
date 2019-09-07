package app.shared.initialization.testing

import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.utils.UUID_Utils

object TestUsers {

  val alice       = User(name = "Alice", favoriteNumber       = 38)
  val bob         = User(name = "Bob", favoriteNumber         = 45)
  val kutya       = User(name = "Kutya", favoriteNumber       = 42)
  val cica        = User(name = "Cica", favoriteNumber        = 137)
  val meresiHiba  = User(name = "MeresiHiba", favoriteNumber  = 369)
  val terezAnya   = User(name = "TerezAnya", favoriteNumber   = 666)
  val jetiLabnyom = User(name = "JetiLabnyom", favoriteNumber = 46)

  val aliceEntity:      Entity[User] = Entity.makeFromValue(alice)
  val bobEntity:        Entity[User] = Entity.makeFromValue(bob)
  val meresiHibaEntity: Entity[User] = Entity.makeFromValue(meresiHiba)
  import monocle.macros.syntax.lens._

  val aliceEntity_with_UUID0 = aliceEntity
    .lens(_.refToEntity.entityIdentity.uuid)
    .set(UUID_Utils.uuid00)

  val bobEntity_with_UUID1 = bobEntity
    .lens(_.refToEntity.entityIdentity.uuid)
    .set(UUID_Utils.uuid01)

  val meresiHiba_with_UUID2 = meresiHibaEntity
    .lens(_.refToEntity.entityIdentity.uuid)
    .set(UUID_Utils.uuid02)

}
