package testingData

import dataModel.{Note, User}
import entity.Entity

object TestEntitiesForNotes {

  val a1: Entity[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  val aliceRef  = a1.ref.entityIdentity

  val note01Alice = Note("Alice note 1", "Super deep story.", aliceRef)

  val note01AliceWithRef= Entity.makeFromValue(note01Alice)

  val note02Alice = Note("Alice note 2", "Very boring story", aliceRef)

  val note02AliceWithRef= Entity.makeFromValue(note02Alice)

  val note03Alice = Note("Alice note 3", "Alice in Wonderland", aliceRef)

  val note03AliceWithRef= Entity.makeFromValue(note03Alice)

}

