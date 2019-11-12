package testingData

import dataStorage.stateHolder.UsersEntities
import dataStorage.{Ref, ReferencedValue, User, Value}
import io.circe.{Decoder, Json}
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

import scala.reflect.ClassTag

object TestApp extends App{

  val alice: Value[User] = TestEntitiesForUsers.alice
  println(alice)

  val res: Json =io.circe.Encoder[Value[User]].apply(alice)

  println(res)

  println(io.circe.Decoder[Value[User]].decodeJson(res))

  val ae=io.circe.Encoder[ReferencedValue[User]].apply(TestEntitiesForUsers.aliceEntity)
  println(ae)

//  println(Ref.name2[User])

  val ue=UsersEntities()
//  ue.insertR(TestEntitiesForUsers.aliceEntity)

}


import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

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


  val aliceEntity: ReferencedValue[User] = ReferencedValue(alice)

  val bobEntity: ReferencedValue[User] = ReferencedValue(bob)

  val meresiHibaEntity: ReferencedValue[User] = ReferencedValue(meresiHiba)

  import monocle.macros.syntax.lens._



  val aliceEntity_with_UUID0 = aliceEntity
    .lens(_.ref.unTypedRef.uuid)
    .set(UUIDs.uuid00)

  val bobEntity_with_UUID1 = bobEntity
    .lens(_.ref.unTypedRef.uuid)
    .set(UUIDs.uuid01)

  val meresiHiba_with_UUID2 = meresiHibaEntity
    .lens(_.ref.unTypedRef.uuid)
    .set(UUIDs.uuid02)

}
