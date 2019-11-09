package testingData

import refs.EntityWithRef
import dataModel.{Note, User}

object TestEntitiesForUsers {

  val alice = User(name = "Alice",
                   favoriteNumber = 38,
                   password       = "titokNyitja")
  val bob         = User(name = "Bob", favoriteNumber         = 45)
  val kutya       = User(name = "Kutya", favoriteNumber       = 42)
  val cica        = User(name = "Cica", favoriteNumber        = 137)
  val meresiHiba  = User(name = "MeresiHiba", favoriteNumber  = 369)
  val terezAnya   = User(name = "TerezAnya", favoriteNumber   = 666)
  val jetiLabnyom = User(name = "JetiLabnyom", favoriteNumber = 46)

  val aliceEntity: EntityWithRef[User] =
    EntityWithRef.makeFromValue(alice)

  val bobEntity: EntityWithRef[User] =
    EntityWithRef.makeFromValue(bob)

  val meresiHibaEntity: EntityWithRef[User] =
    EntityWithRef.makeFromValue(meresiHiba)
  import monocle.macros.syntax.lens._

  val aliceEntity_with_UUID0 = aliceEntity
    .lens(_.toRef.entityIdentity.uuid)
    .set(UUIDs.uuid00)

  val bobEntity_with_UUID1 = bobEntity
    .lens(_.toRef.entityIdentity.uuid)
    .set(UUIDs.uuid01)

  val meresiHiba_with_UUID2 = meresiHibaEntity
    .lens(_.toRef.entityIdentity.uuid)
    .set(UUIDs.uuid02)

}

