package testingData

import dataModel.{Note, User}
import refs.ValueWithIdentityAndVersion

object TestEntitiesForNotes {

  val a1: ValueWithIdentityAndVersion[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  val aliceRef  = a1.ref.entityIdentity

  val note01Alice = Note("Alice note 1", "Super deep story.", aliceRef)

  val note01AliceWithRef= ValueWithIdentityAndVersion.makeFromValue(note01Alice)

  val note02Alice = Note("Alice note 2", "Very boring story", aliceRef)

  val note02AliceWithRef= ValueWithIdentityAndVersion.makeFromValue(note02Alice)

  val note03Alice = Note("Alice note 3", "Alice in Wonderland", aliceRef)

  val note03AliceWithRef= ValueWithIdentityAndVersion.makeFromValue(note03Alice)

}

