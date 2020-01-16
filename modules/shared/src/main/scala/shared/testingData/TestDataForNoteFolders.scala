package shared.testingData

import shared.dataStorage.{TypedReferencedValue, User}
import shared.testingData.TestDataStore.aliceUserEnt

object TestDataForNoteFolders {
  import shared.dataStorage.NoteFolder

  lazy val aliceEnt: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  lazy val ar = aliceEnt.ref


  val noteFolderOne=NoteFolder("Note Folder One")
  val noteFolderTwo=NoteFolder("Note Folder Two")

  val noteFolderOneWithRef= TypedReferencedValue(noteFolderOne).addEntityOwnerInfo(ar)
  val noteFolderTwoWithRef= TypedReferencedValue(noteFolderTwo).addEntityOwnerInfo(ar)

}
