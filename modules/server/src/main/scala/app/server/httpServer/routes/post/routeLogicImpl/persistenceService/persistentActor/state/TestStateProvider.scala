package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.state

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state.StateMapSnapshot
import app.shared.initialization.testing.TestUsers
import io.circe.generic.auto._

case class TestStateProvider(applicationStateMap: StateMapSnapshot)

object TestStateProvider {
  def getTestState: StateMapSnapshot = {
    val init: StateMapSnapshot = StateMapSnapshot()
    val withAlice = init.insertEntity(TestUsers.aliceEntity_with_UUID0)
    val withBob   = withAlice.insertEntity(TestUsers.bobEntity_with_UUID1)
    withBob
  }

}
