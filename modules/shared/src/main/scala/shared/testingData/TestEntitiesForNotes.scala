package shared.testingData

import shared.dataStorage.{Note, TypedReferencedValue, User}

object TestEntitiesForNotes {

  val a1: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  val aliceRef = a1.ref

  lazy val nfr = TestDataForNoteFolders.noteFolderOneWithRef.ref
//  val nf1_00 = Some(LocationInNoteFolder(nfr, 0))
//  val nf1_01 = Some(LocationInNoteFolder(nfr, 1))
//  val nf1_02 = Some(LocationInNoteFolder(nfr, 2))

  lazy val note01Alice: Note =
    shared.dataStorage.Note("Alice note 1", "Super deep story.")

  lazy val note01AliceWithRef: TypedReferencedValue[Note] =
    TypedReferencedValue(note01Alice)

  lazy val note02Alice =
    shared.dataStorage.Note("Alice note 2", "Very boring story")

  lazy val note02AliceWithRef = TypedReferencedValue(note02Alice)

  lazy val note03Alice =
    shared.dataStorage.Note("Alice note 3", "Alice in Wonderland")

  lazy val note03AliceWithRef = TypedReferencedValue(note03Alice)

}
