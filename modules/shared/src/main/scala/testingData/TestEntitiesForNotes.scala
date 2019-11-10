package testingData

import dataModel.{Note, User}
import entity.EntityValueWithVersionAndIdentity

object TestEntitiesForNotes {

  val a1: EntityValueWithVersionAndIdentity[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  val aliceRef  = a1.ref

  val note01Alice: Note = Note("Alice note 1", "Super deep story.", aliceRef)

  val note01AliceWithRef= EntityValueWithVersionAndIdentity(note01Alice)

  val note02Alice = Note("Alice note 2", "Very boring story", aliceRef)

  val note02AliceWithRef= EntityValueWithVersionAndIdentity(note02Alice)

  val note03Alice = Note("Alice note 3", "Alice in Wonderland", aliceRef)

  val note03AliceWithRef= EntityValueWithVersionAndIdentity(note03Alice)

}

