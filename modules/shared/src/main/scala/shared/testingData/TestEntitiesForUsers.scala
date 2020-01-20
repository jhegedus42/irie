package shared.testingData

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shared.dataStorage.model.User
import shared.dataStorage.relationalWrappers
import shared.dataStorage.relationalWrappers.TypedReferencedValue

object TestEntitiesForUsers {

  val alice =
    User(name           = "Alice",
         favoriteNumber = 38,
         password       = "titokNyitja")
  val bob   = User(name = "Bob", favoriteNumber   = 45)
  val kutya = User(name = "Kutya", favoriteNumber = 42)
  val cica  = User(name = "Cica", favoriteNumber  = 137)

  val meresiHiba =
    User(name = "MeresiHiba", favoriteNumber = 369)

  val terezAnya =
    User(name = "TerezAnya", favoriteNumber = 666)

  val jetiLabnyom =
    User(name = "JetiLabnyom", favoriteNumber = 46)

  val aliceEntity: TypedReferencedValue[User] =
    relationalWrappers.TypedReferencedValue(alice)

  val bobUserEntity: TypedReferencedValue[User] =
    relationalWrappers.TypedReferencedValue(bob)

  val meresiHibaEntity: TypedReferencedValue[User] =
    relationalWrappers.TypedReferencedValue(
      meresiHiba
    )

  val terezAnyaUserEntity: TypedReferencedValue[User] =
    relationalWrappers.TypedReferencedValue(
      terezAnya
    )

  val jetiLabnyomUserEntity: TypedReferencedValue[User] =
    relationalWrappers.TypedReferencedValue(
      jetiLabnyom
    )

  import monocle.macros.syntax.lens._

  val aliceEntity_with_UUID0 = aliceEntity
    .lens(_.ref.unTypedRef.uuid)
    .set(UUIDs.uuid00)

  val bobEntity_with_UUID1 = bobUserEntity
    .lens(_.ref.unTypedRef.uuid)
    .set(UUIDs.uuid01)

  val meresiHiba_with_UUID2 = meresiHibaEntity
    .lens(_.ref.unTypedRef.uuid)
    .set(UUIDs.uuid02)

}
