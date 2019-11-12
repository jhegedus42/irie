package testingData

import dataStorage.{Note, ReferencedValue, User}

object TestEntitiesForNotes {

  val a1: ReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  val aliceRef  = a1.ref

  val note01Alice: Note = Note("Alice note 1", "Super deep story.", aliceRef)

  val note01AliceWithRef= ReferencedValue(note01Alice)

  val note02Alice = Note("Alice note 2", "Very boring story", aliceRef)

  val note02AliceWithRef= ReferencedValue(note02Alice)

  val note03Alice = Note("Alice note 3", "Alice in Wonderland", aliceRef)

  val note03AliceWithRef= ReferencedValue(note03Alice)

}

