package shared.testingData

import io.circe.Json
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.Encoder
import io.circe.parser._
import io.circe._
import shared.dataStorage.model.{User, Value}
import shared.dataStorage.stateHolder.EntityStorage
import shared.dataStorage.relationalWrappers.TypedReferencedValue

object TestDataStore extends App {

  lazy val alice: Value[User] = TestEntitiesForUsers.alice
  println(alice)

//  lazy val res: Json = {
//    io.circe.Encoder[Value[User]].apply(alice)
//
//
//
//  }

//  println(res)

//  println(io.circe.Decoder[Value[User]].decodeJson(res))

  lazy val ue = EntityStorage()

  lazy val aliceUserEnt: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  lazy val mh =
    TestEntitiesForUsers.meresiHiba_with_UUID2.ref

  lazy val ar = aliceUserEnt.ref

  import TestEntitiesForUsers._

  def testData: EntityStorage =
    ue.insertHelper(aliceUserEnt.addEntityOwnerInfo(ar))
      .insertHelper(bobUserEntity.addEntityOwnerInfo(ar))
      .insertHelper(terezAnyaUserEntity.addEntityOwnerInfo(ar))
      .insertHelper(
        jetiLabnyomUserEntity.addEntityOwnerInfo(mh)
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
      .insertHelper(TestDataForNoteFolders.noteFolderOneWithRef)
      .insertHelper(TestDataForNoteFolders.noteFolderTwoWithRef)

}
