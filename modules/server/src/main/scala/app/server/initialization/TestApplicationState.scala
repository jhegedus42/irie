package app.server.initialization

import app.server.httpServer.routes.persistenceProvider.persistentActor.state.ApplicationStateMap
import app.shared.initialization.testing.TestUsers
import io.circe.generic.auto._

case class TestApplicationState ( applicationStateMap: ApplicationStateMap)

object TestApplicationState {
  def getTestState:TestApplicationState ={
    val init=ApplicationStateMap()
    val withAlice=init.insertEntity(TestUsers.aliceEntity_with_UUID0)
    val withBob=withAlice.insertEntity(TestUsers.bobEntity_with_UUID1)
    TestApplicationState(withBob)
  }

}

