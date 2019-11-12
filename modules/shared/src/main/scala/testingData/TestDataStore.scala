package testingData

import dataStorage.stateHolder.EntityStorage
import dataStorage.{ReferencedValue, User, Value}
import io.circe.Json


import dataStorage.stateHolder.EntityStorage
import dataStorage.{Ref, ReferencedValue, User, Value}
import io.circe.{Decoder, Json}
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

import scala.reflect.ClassTag


object TestDataStore extends App{

  val alice: Value[User] = TestEntitiesForUsers.alice
  println(alice)

  val res: Json =io.circe.Encoder[Value[User]].apply(alice)

  println(res)

  println(io.circe.Decoder[Value[User]].decodeJson(res))

  val ae=io.circe.Encoder[ReferencedValue[User]].apply(TestEntitiesForUsers.aliceEntity)
  println(ae)

//  println(Ref.name2[User])

  val ue=EntityStorage()

  val aliceEnt=TestEntitiesForUsers.aliceEntity

  import TestEntitiesForUsers._
  lazy val testData: EntityStorage = ue.
    insert(aliceEnt.ref.unTypedRef,aliceEnt.asJson).
    insertHelper(bobEntity)
    .insertHelper(meresiHibaEntity)


}
