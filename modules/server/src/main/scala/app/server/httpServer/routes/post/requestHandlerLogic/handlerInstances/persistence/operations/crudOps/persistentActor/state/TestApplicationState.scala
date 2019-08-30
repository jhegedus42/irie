package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.state

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.state.StateMapSnapshot
import app.shared.initialization.testing.TestUsers

case class TestApplicationState ( applicationStateMap: StateMapSnapshot)

object TestApplicationState {
  def getTestState:TestApplicationState ={
    val init: StateMapSnapshot =StateMapSnapshot()

//    val withAlice=init.insertEntity(TestUsers.aliceEntity_with_UUID0)
//    val withBob=withAlice.insertEntity(TestUsers.bobEntity_with_UUID1)
//    TestApplicationState(withBob)

    TestApplicationState(init)  // todo-right-now
  }

}

