package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.persistentActor.logic

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.persistentActor.data.state.{StateMapEntry, StateMapSnapshot, UntypedRef}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.persistentActor.state.TestStateProvider
import app.server.initialization.Config





private[logic] case class StateService(
    ) {

  val areWeTesting = Config.getDefaultConfig.areWeTesting

  private var applicationState: StateMapSnapshot =
    if (areWeTesting) TestStateProvider.getTestState else
      new StateMapSnapshot()


  def getState = applicationState

  def setNewState(s: StateMapSnapshot): Unit = {
    println("\n\nState was set to:\n")
    //    StatePrintingUtils.printApplicationState( s )
    applicationState = s
  }

}
