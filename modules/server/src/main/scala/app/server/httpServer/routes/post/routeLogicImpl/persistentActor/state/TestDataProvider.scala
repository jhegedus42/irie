package app.server.httpServer.routes.post.routeLogicImpl.persistentActor.state

import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state.{
  StateMapSnapshot,
  UntypedEntityWithRef
}
import app.shared.initialization.testing.TestEntitiesForUsers
import io.circe.generic.auto._

case class TestDataProvider(applicationStateMap: StateMapSnapshot)

object TestDataProvider {

  def getTestState: StateMapSnapshot = {
    val init: StateMapSnapshot = StateMapSnapshot()

    val aliceRef: UntypedEntityWithRef =
      UntypedEntityWithRef.makeFromEntity(
        TestEntitiesForUsers.aliceEntity_with_UUID0
      )

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
  }

}
