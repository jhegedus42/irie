package app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.state

import app.shared.initialization.testing.TestUsers

case class TestApplicationState ( applicationStateMap: ApplicationStateMap)

object TestApplicationState {
  def getTestState:TestApplicationState ={
    val init: ApplicationStateMap =ApplicationStateMap()
//    val withAlice=init.insertEntity(TestUsers.aliceEntity_with_UUID0)
//    val withBob=withAlice.insertEntity(TestUsers.bobEntity_with_UUID1)
//    TestApplicationState(withBob)
    ??? //todo-now
  }

}

