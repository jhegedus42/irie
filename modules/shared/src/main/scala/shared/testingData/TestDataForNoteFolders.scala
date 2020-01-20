package shared.testingData

import shared.dataStorage.model.User
import shared.dataStorage.relationalWrappers.TypedReferencedValue
import shared.testingData.TestDataStore.aliceUserEnt

object TestDataForNoteFolders {

  import shared.dataStorage.model.Folder

  lazy val aliceEnt: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  lazy val ar = aliceEnt.ref

  lazy val noteFolderOne = Folder(
    "Note Folder One",
    List(
      TestEntitiesForNotes.note02AliceWithRef.ref
        .addTypeInfo().addEntityOwnerInfo(ar),
      TestEntitiesForNotes.note01AliceWithRef.ref
        .addTypeInfo().addEntityOwnerInfo(ar)
    )
  )
  lazy val noteFolderTwo = Folder("Note Folder Two", List())

  lazy val noteFolderOneWithRef =
    TypedReferencedValue(noteFolderOne).addEntityOwnerInfo(ar)

  lazy val noteFolderTwoWithRef =
    TypedReferencedValue(noteFolderTwo).addEntityOwnerInfo(ar)

}
