package shared.testingData

import shared.dataStorage.model.CanProvideDefaultValue.defValOf
import shared.dataStorage.model.{
  CanProvideDefaultValue,
  HintForNote,
  Note,
  User
}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

object TestEntitiesForNotes {

  val a1: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  val aliceRef = a1.ref

  lazy val nfr = TestDataForNoteFolders.noteFolderOneWithRef.ref
//  val nf1_00 = Some(LocationInNoteFolder(nfr, 0))
//  val nf1_01 = Some(LocationInNoteFolder(nfr, 1))
//  val nf1_02 = Some(LocationInNoteFolder(nfr, 2))

  lazy val note01Alice: Note =
    shared.dataStorage.model.Note("Alice note 1",
                                  "Super deep story.",
                                  defValOf[HintForNote])

  lazy val note01AliceWithRef: TypedReferencedValue[Note] =
    TypedReferencedValue(note01Alice)

  lazy val note02Alice =
    shared.dataStorage.model.Note("Alice note 2",
                                  "Very boring story",
                                  defValOf[HintForNote])

  lazy val note02AliceWithRef = TypedReferencedValue(note02Alice)

  import CanProvideDefaultValue.defValOf

  lazy val note03Alice =
    shared.dataStorage.model.Note("Alice note 3",
                                  "Alice in Wonderland",
                                  defValOf[HintForNote])

  lazy val note03AliceWithRef = TypedReferencedValue(note03Alice)

}
