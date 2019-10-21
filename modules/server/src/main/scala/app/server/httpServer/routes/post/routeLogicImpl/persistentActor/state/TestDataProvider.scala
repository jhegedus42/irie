package app.server.httpServer.routes.post.routeLogicImpl.persistentActor.state

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.initialization.testing.TestEntitiesForUsers
import app.shared.state.{StateMapSnapshot, UntypedEntityWithRef}
import io.circe.generic.auto._

object TestEntitiesForNotes {

  val a1: EntityWithRef[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0
  val aliceRef: RefToEntityWithVersion[User] = a1.refToEntity
  val note01Alice = Note("Alice note 1", "Super deep story.", aliceRef)
  val note02Alice = Note("Alice note 2", "Very boring story", aliceRef)
  val note03Alice = Note("Alice note 3", "Alice in Wonderland", aliceRef)

}

case class TestDataProvider(applicationStateMap: StateMapSnapshot)

object TestDataProvider {


  val aliceRef: UntypedEntityWithRef =
    UntypedEntityWithRef.makeFromEntityWithRef(
      TestEntitiesForUsers.aliceEntity_with_UUID0
    )

  def getTestState: StateMapSnapshot = {
    val init: StateMapSnapshot = StateMapSnapshot()

    val withAlice = init.insertVirginEntity(aliceRef)

    val withBob =
      withAlice.insertVirginEntity(
        TestEntitiesForUsers.bobEntity_with_UUID1
      )

    val withMeresiHiba =
      withBob.insertVirginEntity(
        TestEntitiesForUsers.meresiHiba_with_UUID2
      )

    withMeresiHiba

    // todo-now 1.1.1.1.1 continue-here - create
  }

}
