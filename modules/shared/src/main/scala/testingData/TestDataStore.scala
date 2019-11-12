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

  lazy val alice: Value[User] = TestEntitiesForUsers.alice
  println(alice)

  lazy val res: Json =io.circe.Encoder[Value[User]].apply(alice)

  println(res)

  println(io.circe.Decoder[Value[User]].decodeJson(res))

  lazy val ae=io.circe.Encoder[ReferencedValue[User]].apply(TestEntitiesForUsers.aliceEntity)
  println(ae)

//  println(Ref.name2[User])

  lazy val ue=EntityStorage()

  lazy val aliceEnt=TestEntitiesForUsers.aliceEntity

  import TestEntitiesForUsers._
  def testData: EntityStorage = ue.
    insert(aliceEnt.ref.unTypedRef,aliceEnt.asJson).
    insertHelper(bobEntity)
    .insertHelper(meresiHibaEntity)


}