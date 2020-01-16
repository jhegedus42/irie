package shared.testingData

import shared.dataStorage.{
  Note,
  TypedReferencedValue,
  User
}

object TestEntitiesForNotes {

  val a1: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  val aliceRef = a1.ref

  val nfr    = TestDataForNoteFolders.noteFolderOneWithRef.ref
//  val nf1_00 = Some(LocationInNoteFolder(nfr, 0))
//  val nf1_01 = Some(LocationInNoteFolder(nfr, 1))
//  val nf1_02 = Some(LocationInNoteFolder(nfr, 2))

  val note01Alice: Note =
    shared.dataStorage.Note("Alice note 1",
                            "Super deep story." )

  val note01AliceWithRef = TypedReferencedValue(note01Alice)

  val note02Alice =
    shared.dataStorage.Note("Alice note 2",
                            "Very boring story")

  val note02AliceWithRef = TypedReferencedValue(note02Alice)

  val note03Alice =
    shared.dataStorage.Note("Alice note 3",
                            "Alice in Wonderland")

  val note03AliceWithRef = TypedReferencedValue(note03Alice)

}
