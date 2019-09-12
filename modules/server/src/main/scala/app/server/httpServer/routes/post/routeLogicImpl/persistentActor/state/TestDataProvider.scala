package app.server.httpServer.routes.post.routeLogicImpl.persistentActor.state

import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state.{
  StateMapSnapshot,
  UntypedEntity
}
import app.shared.initialization.testing.TestUsers
import io.circe.generic.auto._

case class TestDataProvider(applicationStateMap: StateMapSnapshot)

object TestDataProvider {
  def getTestState: StateMapSnapshot = {
    val init: StateMapSnapshot = StateMapSnapshot()

    val withAlice = init.insertVirginEntity(
      UntypedEntity.makeFromEntity(TestUsers.aliceEntity_with_UUID0)
    )
    val withBob =
      withAlice.insertVirginEntity(TestUsers.bobEntity_with_UUID1)

    val withMeresiHiba =
      withBob.insertVirginEntity(TestUsers.meresiHiba_with_UUID2)
    withMeresiHiba
  }

}