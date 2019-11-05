package app.shared.state

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.initialization.testing.TestEntitiesForUsers

object TestEntitiesForNotes {

  val a1: EntityWithRef[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  val aliceRef  = a1.toRef.entityIdentity

  val note01Alice = Note("Alice note 1", "Super deep story.", aliceRef)

  val note01AliceWithRef= EntityWithRef.makeFromValue(note01Alice)

  val note02Alice = Note("Alice note 2", "Very boring story", aliceRef)

  val note02AliceWithRef= EntityWithRef.makeFromValue(note02Alice)

  val note03Alice = Note("Alice note 3", "Alice in Wonderland", aliceRef)

  val note03AliceWithRef= EntityWithRef.makeFromValue(note03Alice)

}

case class TestDataProvider(applicationStateMap: StateMapSnapshot)

object TestDataProvider {


  val aliceRef: UntypedEntityWithRef =
    UntypedEntityWithRef.makeFromEntityWithRef(
      TestEntitiesForUsers.aliceEntity_with_UUID0
    )

  def getTestState: StateMapSnapshot = {
    val init: StateMapSnapshot = StateMapSnapshot()

//    val withAlice = init.insertVirginEntity(aliceRef)
//
//    val withBob =
//      withAlice.insertVirginEntity(
//        TestEntitiesForUsers.bobEntity_with_UUID1
//      )
//
//    val withMeresiHiba =
//      withBob.insertVirginEntity(
//        TestEntitiesForUsers.meresiHiba_with_UUID2
//      )
    import TestEntitiesForNotes._
    import TestEntitiesForUsers._

    init.
      insertVirginEntity(note01AliceWithRef).
      insertVirginEntity(note03AliceWithRef).
      insertVirginEntity(bobEntity_with_UUID1).
      insertVirginEntity(aliceEntity_with_UUID0).
      insertVirginEntity((meresiHiba_with_UUID2))

  }

}
