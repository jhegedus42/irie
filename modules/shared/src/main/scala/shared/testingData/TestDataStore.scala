package shared.testingData

import io.circe.Json
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shared.dataStorage.stateHolder.EntityStorage
import shared.dataStorage.{TypedReferencedValue, User, Value}

object TestDataStore extends App {

  lazy val alice: Value[User] = TestEntitiesForUsers.alice
  println(alice)

  lazy val res: Json =
    io.circe.Encoder[Value[User]].apply(alice)

  println(res)

  println(io.circe.Decoder[Value[User]].decodeJson(res))

  lazy val ue = EntityStorage()

  lazy val aliceEnt: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  lazy val mh =
    TestEntitiesForUsers.meresiHiba_with_UUID2.ref

  lazy val ar = aliceEnt.ref

  import TestEntitiesForUsers._

  def testData: EntityStorage =
    ue.insertHelper(aliceEnt.addEntityOwnerInfo(ar))
      .insertHelper(bobEntity.addEntityOwnerInfo(ar))
      .insertHelper(terezAnyaEntity.addEntityOwnerInfo(ar))
      .insertHelper(
        jetiLabnyomEntity.addEntityOwnerInfo(mh)
      )
      .insertHelper(
        TestEntitiesForNotes.note01AliceWithRef.addEntityOwnerInfo(ar)
      )
      .insertHelper(
        TestEntitiesForNotes.note02AliceWithRef.addEntityOwnerInfo(ar)
      )
      .insertHelper(
        TestEntitiesForNotes.note03AliceWithRef.addEntityOwnerInfo(ar)
      )
      .insertHelper(TestDataForImages.appleImage)
      .insertHelper(TestDataForImages.starImage)
      .insertHelper(TestDataForImages.musicImage)

}
