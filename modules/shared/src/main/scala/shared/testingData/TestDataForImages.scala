package shared.testingData

import shared.dataStorage.{Image, TypedReferencedValue, User}
import shared.testingData.TestDataStore.aliceUserEnt

object TestDataForImages {

  lazy val aliceEnt: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  lazy val ar = aliceEnt.ref

  val appleImage = TypedReferencedValue(Image("apple", None,None))
    .addEntityOwnerInfo(ar)

  val starImage = TypedReferencedValue(Image("star", None,None))
    .addEntityOwnerInfo(ar)

  val musicImage = TypedReferencedValue(Image("music", None,None))
    .addEntityOwnerInfo(ar)

}
